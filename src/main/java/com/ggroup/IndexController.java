package com.ggroup;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  // توجيه كل الطلبات التي لا تحتوي على امتداد (مثل /about أو /contact) إلى
  // index.html
  @RequestMapping(value = "/{path:[^\\.]*}")
  public String redirect() {
    return "forward:/index.html"; // إعادة توجيه كل الطلبات إلى index.html
  }
}
