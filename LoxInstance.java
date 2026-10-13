package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class LoxInstance {
  private LoxClass klass;
  private final Map<String, Object> fields = new HashMap<>();

  LoxInstance(LoxClass klass) {
    this.klass = klass;
  }

  // ch 12 q2: takes the interpreter so getters can be invoked on access.
  Object get(Token name, Interpreter interpreter) {
    if (fields.containsKey(name.lexeme)) {
      return fields.get(name.lexeme);
    }

    // ch 12 q1
    LoxFunction method = (klass == null) ? null : klass.findMethod(name.lexeme);
    if (method != null) {
      LoxFunction bound = method.bind(this);
      // ch 12 q2
      if (bound.isGetter()) {
        return bound.call(interpreter, new java.util.ArrayList<>());
      }
      return bound;
    }

    throw new RuntimeError(name,
        "Undefined property '" + name.lexeme + "'.");
  }

  void set(Token name, Object value) {
    fields.put(name.lexeme, value);
  }

  @Override
  public String toString() {
    return klass.name + " instance";
  }
}
