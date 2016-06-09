package de.plushnikov.intellij.plugin.processor.modifier;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import de.plushnikov.intellij.plugin.processor.ValProcessor;

/**
 * @author Alexej Kubarev
 */
public class ValModifierProcessor implements ModifierProcessor {

  @Override
  public boolean isSupported(@NotNull PsiModifierList modifierList) {

    final PsiElement parent = modifierList.getParent();

    return (parent instanceof PsiLocalVariable && ValProcessor.isVal((PsiLocalVariable) parent));
  }

  @Override
  @NotNull
  public Set<String> transformModifiers(@NotNull PsiModifierList modifierList, @NotNull final Set<String> modifiers) {
    modifiers.add(PsiModifier.FINAL);

    return modifiers;
  };
}
