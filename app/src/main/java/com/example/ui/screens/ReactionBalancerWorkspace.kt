package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.model.StudyLanguage

private data class BalanceSpecies(val formula: String, val reactant: Boolean)

@Composable
fun ReactionBalancerWorkspace(language: StudyLanguage) {
    val kannada = language == StudyLanguage.KANNADA
    val examples = listOf(
        listOf(BalanceSpecies("H2", true), BalanceSpecies("O2", true), BalanceSpecies("H2O", false)),
        listOf(BalanceSpecies("Fe", true), BalanceSpecies("O2", true), BalanceSpecies("Fe2O3", false)),
        listOf(BalanceSpecies("Al", true), BalanceSpecies("HCl", true), BalanceSpecies("AlCl3", false), BalanceSpecies("H2", false)),
        listOf(BalanceSpecies("Ca(OH)2", true), BalanceSpecies("HCl", true), BalanceSpecies("CaCl2", false), BalanceSpecies("H2O", false))
    )
    val labels = if (kannada) listOf("ಹೈಡ್ರೋಜನ್ ದಹನ", "ಕಬ್ಬಿಣದ ಆಕ್ಸೈಡ್", "ಅಲ್ಯೂಮಿನಿಯಂ ಮತ್ತು ಆಮ್ಲ", "ತಟಸ್ಥೀಕರಣ") else listOf("Hydrogen combustion", "Iron oxide", "Aluminium + acid", "Neutralisation")
    val formulas = remember { mutableStateListOf("H2", "O2", "H2O") }
    val sides = remember { mutableStateListOf(true, true, false) }
    val coefficients = remember { mutableStateListOf(2, 1, 2) }
    Card(shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(if (kannada) "ರಾಸಾಯನಿಕ ಸಮೀಕರಣ ಸಮತೋಲನ ಅಭ್ಯಾಸ" else "Interactive Reaction Balancer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(if (kannada) "ಗುಣಾಂಕಗಳನ್ನು ನಮೂದಿಸಿ ಅಥವಾ + / − ಬಳಸಿ. ಎರಡೂ ಬದಿಗಳಲ್ಲಿನ ಪರಮಾಣುಗಳ ಸಂಖ್ಯೆಯನ್ನು ಪರಿಶೀಲಿಸಿ." else "Enter coefficients or use + / −. Compare live atom totals on both sides.", style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                examples.forEachIndexed { index, _ ->
                    TextButton(onClick = {
                        formulas.clear(); sides.clear(); coefficients.clear()
                        examples[index].forEach { formulas.add(it.formula); sides.add(it.reactant); coefficients.add(1) }
                    }) { Text("\${index + 1}") }
                }
            }
            Text((if (kannada) "ಉದಾಹರಣೆಗಳು: " else "Examples: ") + labels.joinToString(" • "), style = MaterialTheme.typography.labelSmall)
            formulas.indices.forEach { i ->
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(formulas[i] + if (sides[i]) "  (LHS)" else "  (RHS)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(
                            value = coefficients[i].toString(),
                            onValueChange = { raw -> raw.toIntOrNull()?.let { coefficients[i] = it.coerceIn(1, 99) } },
                            label = { Text(if (kannada) "ಗುಣಾಂಕ" else "Coefficient") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column {
                        TextButton(onClick = { coefficients[i] = (coefficients[i] + 1).coerceAtMost(99) }) { Text("+") }
                        TextButton(onClick = { coefficients[i] = (coefficients[i] - 1).coerceAtLeast(1) }) { Text("−") }
                    }
                }
            }
            val atomCounts = formulas.indices.mapNotNull { i ->
                parseFormula(formulas[i])?.let { counts -> i to counts.mapValues { it.value * coefficients[i] } }
            }
            val left = mutableMapOf<String, Int>()
            val right = mutableMapOf<String, Int>()
            atomCounts.forEach { (i, counts) ->
                val target = if (sides[i]) left else right
                counts.forEach { (element, count) -> target[element] = (target[element] ?: 0) + count }
            }
            val valid = atomCounts.size == formulas.size
            val balanced = valid && left == right
            Surface(color = if (balanced) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.fillMaxWidth().padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(if (balanced) (if (kannada) "ಸಮತೋಲಿತ ಸಮೀಕರಣ ✓" else "Balanced equation ✓") else if (kannada) "ಇನ್ನೂ ಸಮತೋಲನವಾಗಿಲ್ಲ" else "Not balanced yet", fontWeight = FontWeight.Bold)
                    Text((if (kannada) "ಎಡಭಾಗ: " else "Left: ") + left.toSortedMap().entries.joinToString { "\${it.key}=\${it.value}" })
                    Text((if (kannada) "ಬಲಭಾಗ: " else "Right: ") + right.toSortedMap().entries.joinToString { "\${it.key}=\${it.value}" })
                    if (!valid) Text(if (kannada) "ಸೂತ್ರವನ್ನು ಪರಿಶೀಲಿಸಿ." else "Check the chemical formula syntax.", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

private fun parseFormula(formula: String): Map<String, Int>? {
    var index = 0
    fun parseGroup(nested: Boolean): MutableMap<String, Int>? {
        val result = mutableMapOf<String, Int>()
        while (index < formula.length) {
            val ch = formula[index]
            if (ch == ')') {
                if (!nested) return null
                index++
                return result
            }
            if (ch == '(') {
                index++
                val inner = parseGroup(true) ?: return null
                val start = index
                while (index < formula.length && formula[index].isDigit()) index++
                val multiplier = formula.substring(start, index).toIntOrNull() ?: 1
                inner.forEach { (element, count) -> result[element] = (result[element] ?: 0) + count * multiplier }
            } else if (ch.isUpperCase()) {
                val start = index++
                while (index < formula.length && formula[index].isLowerCase()) index++
                val element = formula.substring(start, index)
                val numberStart = index
                while (index < formula.length && formula[index].isDigit()) index++
                val count = formula.substring(numberStart, index).toIntOrNull() ?: 1
                result[element] = (result[element] ?: 0) + count
            } else return null
        }
        return if (nested) null else result
    }
    val parsed = parseGroup(false) ?: return null
    return parsed.takeIf { it.isNotEmpty() && index == formula.length }
}
