/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.lang.xpath;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.application.PathManager;
import com.intellij.testFramework.LexerTestCase;
import org.jetbrains.annotations.NotNull;

public class XPathLexerTest extends LexerTestCase {
  @Override
  protected @NotNull Lexer createLexer() {
    return XPathLexer.create(false);
  }

  @Override
  protected @NotNull String getDirPath() {
    return TestBase.getTestDataPath("xpath/parsing/lexer").substring(PathManager.getHomePath().length());
  }

  public void testAttributeAxis() {
    doTest("attribute::*");
  }

  public void testBadAxis() {
    doTest("something::*");
  }

  public void testAttributeNodeType() {
    doTest("attribute()");
  }

  public void testElementNodeType() {
    doTest("element()");
  }

  public void testAttributeNCName() {
    doTest("attribute/*");
  }

  public void testElementNCName() {
    doTest("element/*");
  }

  public void testPrefixedNameAnd() {
    doTest("child::xsd:element and contains('a', 'a')");
  }
}