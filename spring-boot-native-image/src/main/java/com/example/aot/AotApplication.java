package com.example.aot;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AotApplication {

  public static void main(String[] args) {
    SpringApplication.run(AotApplication.class, args);
    int[] array = {
      1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1,
      2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
    };
    Arrays.stream(array)
        .mapToObj(String::valueOf)
        .map(String::strip)
        .map(String::stripTrailing)
        .map(String::length)
        .forEach(System.out::println);
    IntStream.range(1, 10).forEach(System.out::println);
    int defined_parameter_with_name_one = 1,
        defined_parameter_with_name_two = 2,
        defined_parameter_with_name_three = 3,
        defined_parameter_with_name_four = 4,
        defined_parameter_with_name_five = 5,
        defined_parameter_with_name_six = 6;
    methodName(
        defined_parameter_with_name_one,
        defined_parameter_with_name_two,
        defined_parameter_with_name_three,
        defined_parameter_with_name_four,
        defined_parameter_with_name_five,
        defined_parameter_with_name_six);
  }

  public static void methodName(
      int parameter_one,
      int parameter_two,
      int parameter_three,
      int parameter_four,
      int parameter_five,
      int parameter_six) {
    System.out.printf("%d%n", parameter_five);
  }

  @RequestMapping(method = RequestMethod.GET, path = "/date")
  public ResponseEntity<String> showServerDate() {
    int a = 0;
    int b = 1;
    int c;

    String text =
        "121112lsmd;almdamsdkmk"
            + "asmkdlmaslmdlamsdlamskldmalksmdlmasldkmaklsmdlmamaskd;"
            + "masmdmasdmalmdlamlkdmalmsdmaalmldmsldmlaksmkdlmasdla";

    text =
        text.replace("1", "2")
            .replace("1", "2")
            .replace("1", "2")
            .replace("1", "2")
            .replace("2", "3")
            .replace("1", "2")
            .replace("3", "4");

    System.out.println(text);

    return ResponseEntity.ok(LocalDateTime.now().toString());
  }
}
