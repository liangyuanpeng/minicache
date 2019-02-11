/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lan.minicache.util;

import org.slf4j.LoggerFactory;

public class LoggerUtil {

  public static void error(String msg) {
    LoggerFactory.getLogger(getClassName()).error(msg);
  }

  public static void error(String msg, Object... obj) {
    LoggerFactory.getLogger(getClassName()).error(msg, obj);
  }

  public static void warn(String msg) {
    LoggerFactory.getLogger(getClassName()).error(msg);
  }

  public static void warn(String msg, Object... obj) {
    LoggerFactory.getLogger(getClassName()).error(msg, obj);
  }

  public static void info(String msg) {
    LoggerFactory.getLogger(getClassName()).info(msg);
  }

  public static void info(String msg, Object... obj) {
    LoggerFactory.getLogger(getClassName()).info(msg, obj);
  }

  public static void debug(String msg) {
    LoggerFactory.getLogger(getClassName()).debug(msg);
  }

  public static void debug(String msg, Object... obj) {
    LoggerFactory.getLogger(getClassName()).debug(msg, obj);
  }

  public static void custom(String msg) {
  }

  // 获取调用 error,info,debug静态类的类名
  private static String getClassName() {
    return new SecurityManager() {
      public String getClassName() {
        return getClassContext()[3].getName();
      }
    }.getClassName();
  }
}
