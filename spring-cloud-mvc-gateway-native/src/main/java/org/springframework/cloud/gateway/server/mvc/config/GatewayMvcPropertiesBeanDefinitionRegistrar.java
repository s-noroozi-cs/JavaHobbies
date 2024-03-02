/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gateway.server.mvc.config;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterDiscoverer;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerDiscoverer;
import org.springframework.cloud.gateway.server.mvc.invoke.InvocationContext;
import org.springframework.cloud.gateway.server.mvc.invoke.OperationArgumentResolver;
import org.springframework.cloud.gateway.server.mvc.invoke.OperationParameters;
import org.springframework.cloud.gateway.server.mvc.invoke.ParameterValueMapper;
import org.springframework.cloud.gateway.server.mvc.invoke.convert.ConversionServiceParameterValueMapper;
import org.springframework.cloud.gateway.server.mvc.invoke.reflect.OperationMethod;
import org.springframework.cloud.gateway.server.mvc.invoke.reflect.ReflectiveOperationInvoker;
import org.springframework.cloud.gateway.server.mvc.predicate.PredicateDiscoverer;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.log.LogMessage;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.*;

public class GatewayMvcPropertiesBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    AbstractBeanDefinition routerFnProviderBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(RouterFunctionHolder.class)
            //                .addConstructorArgReference(getBeanDefinitionName(registry,
            // Environment.class))
            //                .addConstructorArgReference(getBeanDefinitionName(registry,
            // GatewayRouteHelper.class))
            .getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(routerFnProviderBeanDefinition,
                "gatewayRouterFunctionHolder");
    String beanName = holder.getBeanName();

    if (registry.containsBeanDefinition(beanName)) {
      registry.removeBeanDefinition(beanName);
    }
    registry.registerBeanDefinition(beanName, holder.getBeanDefinition());
    AbstractBeanDefinition routerFunctionBeanDefinition =
        BeanDefinitionBuilder.genericBeanDefinition(DelegatingRouterFunction.class)
            .getBeanDefinition();
        registry.registerBeanDefinition("gatewayCompositeRouterFunction", routerFunctionBeanDefinition);
    }

  /** Delegating RouterFunction impl that delegates to the refreshable RouterFunctionHolder. */
  static class DelegatingRouterFunction implements RouterFunction<ServerResponse> {

    final RouterFunctionHolder provider;

    DelegatingRouterFunction(RouterFunctionHolder provider) {
      this.provider = provider;
    }

    @Override
    public RouterFunction<ServerResponse> and(RouterFunction<ServerResponse> other) {
      return this.provider.getRouterFunction().and(other);
    }

    @Override
    public RouterFunction<?> andOther(RouterFunction<?> other) {
      return this.provider.getRouterFunction().andOther(other);
    }

    @Override
    public RouterFunction<ServerResponse> andRoute(
        RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
      return this.provider.getRouterFunction().andRoute(predicate, handlerFunction);
    }

    @Override
    public RouterFunction<ServerResponse> andNest(
        RequestPredicate predicate, RouterFunction<ServerResponse> routerFunction) {
      return this.provider.getRouterFunction().andNest(predicate, routerFunction);
    }

    @Override
    public <S extends ServerResponse> RouterFunction<S> filter(
        HandlerFilterFunction<ServerResponse, S> filterFunction) {
      return this.provider.getRouterFunction().filter(filterFunction);
    }

    @Override
    public void accept(RouterFunctions.Visitor visitor) {
      this.provider.getRouterFunction().accept(visitor);
    }

    @Override
    public RouterFunction<ServerResponse> withAttribute(String name, Object value) {
      return this.provider.getRouterFunction().withAttribute(name, value);
        }

    @Override
    public RouterFunction<ServerResponse> withAttributes(
        Consumer<Map<String, Object>> attributesConsumer) {
      return this.provider.getRouterFunction().withAttributes(attributesConsumer);
        }

    @Override
    public Optional<HandlerFunction<ServerResponse>> route(ServerRequest request) {
      return this.provider.getRouterFunction().route(request);
    }

    @Override
    public String toString() {
      return this.provider.getRouterFunction().toString();
    }
  }
}

class GatewayRouteHelper {

  private final Log log = LogFactory.getLog(getClass());

  private final OperationArgumentResolver trueNullOperationArgumentResolver =
      new OperationArgumentResolver() {

        @Override
        public boolean canResolve(Class<?> type) {
          return false;
        }

        @Override
        public <T> T resolve(Class<T> type) {
          return null;
        }
      };

  private final ParameterValueMapper parameterValueMapper =
      new ConversionServiceParameterValueMapper();

  private final FilterDiscoverer filterDiscoverer = new FilterDiscoverer();

  private final HandlerDiscoverer handlerDiscoverer = new HandlerDiscoverer();

  private final PredicateDiscoverer predicateDiscoverer = new PredicateDiscoverer();

  private <T> void translate(
      MultiValueMap<String, OperationMethod> operations,
      String operationName,
      Map<String, String> operationArgs,
      Class<T> returnType,
      Consumer<T> operationHandler) {
    String normalizedName = StringUtils.uncapitalize(operationName);
    Optional<NormalizedOperationMethod> operationMethod =
        findOperation(operations, normalizedName, operationArgs);
    if (operationMethod.isPresent()) {
      NormalizedOperationMethod opMethod = operationMethod.get();
      T handlerFilterFunction = invokeOperation(opMethod, opMethod.getNormalizedArgs());
      if (handlerFilterFunction != null) {
        operationHandler.accept(handlerFilterFunction);
      }
    } else {
      throw new IllegalArgumentException(
          String.format(
              "Unable to find operation %s for %s with args %s",
              returnType, normalizedName, operationArgs));
    }
  }

  private Optional<NormalizedOperationMethod> findOperation(
      MultiValueMap<String, OperationMethod> operations,
      String operationName,
      Map<String, String> operationArgs) {
    return operations.getOrDefault(operationName, Collections.emptyList()).stream()
        .map(operationMethod -> new NormalizedOperationMethod(operationMethod, operationArgs))
        .filter(opeMethod -> matchOperation(opeMethod, operationArgs))
        .findFirst();
  }

  private static boolean matchOperation(
      NormalizedOperationMethod operationMethod, Map<String, String> args) {
    Map<String, String> normalizedArgs = operationMethod.getNormalizedArgs();
    OperationParameters parameters = operationMethod.getParameters();
    if (parameters.getParameterCount() != normalizedArgs.size()) {
      return false;
    }
    for (int i = 0; i < parameters.getParameterCount(); i++) {
      if (!normalizedArgs.containsKey(parameters.get(i).getName())) {
        return false;
      }
    }
    // args contains all parameter names
    return true;
  }

  private <T> T invokeOperation(
      OperationMethod operationMethod, Map<String, String> operationArgs) {
    Map<String, Object> args = new HashMap<>(operationArgs);
    ReflectiveOperationInvoker operationInvoker =
        new ReflectiveOperationInvoker(operationMethod, this.parameterValueMapper);
    InvocationContext context = new InvocationContext(args, trueNullOperationArgumentResolver);
    return operationInvoker.invoke(context);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @NonNull
  RouterFunction getRealRouterFunction(RouteProperties routeProperties, String routeId) {
        log.trace(LogMessage.format("Creating route for : %s", routeProperties));

        RouterFunctions.Builder builder = route(routeId);

        // MVC.fn users won't need this anonymous filter as url will be set directly.
        // Put this function first, so if a filter from a handler changes the url
        // it is after this one.
        builder.filter((request, next) -> {
            MvcUtils.setRequestUrl(request, routeProperties.getUri());
            return next.handle(request);
        });
        builder.before(BeforeFilterFunctions.routeId(routeId));

        MultiValueMap<String, OperationMethod> handlerOperations = handlerDiscoverer.getOperations();
        // TODO: cache?
        // translate handlerFunction
        String scheme = routeProperties.getUri().getScheme();
        Map<String, String> handlerArgs = new HashMap<>();
        // TODO: avoid hardcoded scheme/uri args
        // maybe find empty args or single RouteProperties param?
        if (scheme.equals("lb")) {
            handlerArgs.put("uri", routeProperties.getUri().toString());
        }
        Optional<NormalizedOperationMethod> handlerOperationMethod = findOperation(handlerOperations,
                scheme.toLowerCase(), handlerArgs);
        if (handlerOperationMethod.isEmpty()) {
            throw new IllegalStateException("Unable to find HandlerFunction for scheme: " + scheme);
        }
        NormalizedOperationMethod normalizedOpMethod = handlerOperationMethod.get();
        Object response = invokeOperation(normalizedOpMethod, normalizedOpMethod.getNormalizedArgs());
        HandlerFunction<ServerResponse> handlerFunction = null;
        if (response instanceof HandlerFunction<?>) {
            handlerFunction = (HandlerFunction<ServerResponse>) response;
    } else if (response instanceof HandlerDiscoverer.Result result) {
            handlerFunction = result.getHandlerFunction();
            result.getFilters().forEach(builder::filter);
        }
        if (handlerFunction == null) {
            throw new IllegalStateException(
                    "Unable to find HandlerFunction for scheme: " + scheme + " and response " + response);
        }

        // translate predicates
        MultiValueMap<String, OperationMethod> predicateOperations = predicateDiscoverer.getOperations();
        final AtomicReference<RequestPredicate> predicate = new AtomicReference<>();

    routeProperties
        .getPredicates()
        .forEach(
            predicateProperties ->
                translate(
                    predicateOperations,
                    predicateProperties.getName(),
                    predicateProperties.getArgs(),
                    RequestPredicate.class,
                    requestPredicate -> {
                      log.trace(
                          LogMessage.format(
                              "Adding predicate to route %s - %s", routeId, predicateProperties));
                      if (predicate.get() == null) {
                        predicate.set(requestPredicate);
                      } else {
                        RequestPredicate combined = predicate.get().and(requestPredicate);
                        predicate.set(combined);
                      }
                      log.trace(
                          LogMessage.format(
                              "Combined predicate for route %s - %s", routeId, predicate.get()));
                    }));

        // combine predicate and handlerFunction
        builder.route(predicate.get(), handlerFunction);
        predicate.set(null);

        // translate filters
        MultiValueMap<String, OperationMethod> filterOperations = filterDiscoverer.getOperations();
        routeProperties.getFilters().forEach(filterProperties -> translate(filterOperations, filterProperties.getName(),
                filterProperties.getArgs(), HandlerFilterFunction.class, builder::filter));

        builder.withAttribute(MvcUtils.GATEWAY_ROUTE_ID_ATTR, routeId);

        return builder.build();
    }
}

class RouterFunctionHolder implements BeanFactoryAware {

  private static final RouterFunction<ServerResponse> NEVER_ROUTE =
      RouterFunctions.route(
          new RequestPredicate() {
            @Override
            public boolean test(ServerRequest request) {
              return false;
            }

            @Override
            public String toString() {
              return "Never";
            }
          },
          request -> ServerResponse.notFound().build());

  protected final Log log = LogFactory.getLog(getClass());
  private final AtomicReference<BeanFactory> beanFactoryAtomicReference = new AtomicReference<>();
  private final AtomicReference<RouterFunction<ServerResponse>> routerFunctionConfigured =
      new AtomicReference<>();

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    System.out.println("setting bean factory for " + getClass().getName());
    this.beanFactoryAtomicReference.set(beanFactory);
  }

  @SuppressWarnings("unchecked")
  private RouterFunction<ServerResponse> init() {

    Environment env = this.beanFactoryAtomicReference.get().getBean(Environment.class);
    GatewayRouteHelper gatewayRouteHelper =
        this.beanFactoryAtomicReference.get().getBean(GatewayRouteHelper.class);

    Assert.notNull(env, "the environment is null");
    Assert.notNull(gatewayRouteHelper, "the gatewayRouteHelper is null");

    GatewayMvcProperties properties =
        Binder.get(env).bindOrCreate(GatewayMvcProperties.PREFIX, GatewayMvcProperties.class);
    log.trace(
        LogMessage.format(
            "RouterFunctionHolder initializing with %d map routes and %d list routes",
            properties.getRoutesMap().size(), properties.getRoutes().size()));

    Map<String, RouterFunction> routerFunctions = new LinkedHashMap<>();
    properties
        .getRoutes()
        .forEach(
            routeProperties -> {
              routerFunctions.put(
                  routeProperties.getId(),
                  gatewayRouteHelper.getRealRouterFunction(
                      routeProperties, routeProperties.getId()));
            });
    properties
        .getRoutesMap()
        .forEach(
            (routeId, routeProperties) -> {
              String computedRouteId = routeId;
              if (StringUtils.hasText(routeProperties.getId())) {
                computedRouteId = routeProperties.getId();
              }
              routerFunctions.put(
                  computedRouteId,
                  gatewayRouteHelper.getRealRouterFunction(routeProperties, computedRouteId));
            });
    RouterFunction routerFunction;
    if (routerFunctions.isEmpty()) {
      // no properties routes, so a RouterFunction that will never match
      routerFunction = NEVER_ROUTE;
    } else {
      routerFunction =
          routerFunctions.values().stream().reduce(RouterFunction::andOther).orElse(null);
      // puts the map of configured RouterFunctions in an attribute. Makes testing
      // easy.
      routerFunction = routerFunction.withAttribute("gatewayRouterFunctions", routerFunctions);
    }
    log.trace(LogMessage.format("RouterFunctionHolder initialized %s", routerFunction.toString()));

    return routerFunction;
  }

  public RouterFunction<ServerResponse> getRouterFunction() {
    if (this.routerFunctionConfigured.get() == null) this.routerFunctionConfigured.set(init());
    return this.routerFunctionConfigured.get();
  }
}
