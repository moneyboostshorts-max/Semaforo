package com.grananovolante.semaforo
enum class CorSemaforo { VERDE, AMARELO, VERMELHO }
data class ResultadoAvaliacao(val cor: CorSemaforo, val valorReais: Double?, val distanciaKm: Double?, val reaisPorKm: Double?, val nota: Double?, val motivo: String)
object MotorRegras {
    fun avaliar(textoCompleto: String, metaKm: Double = 2.0, metaHora: Double = 40.0, metaNota: Double = 4.80): ResultadoAvaliacao {
        val regPreco = Regex("""R\$\s*(\d+[,.]\d{2})""")
        val regKm = Regex("""(\d+[,.]?\d*)\s*km""", RegexOption.IGNORE_CASE)
        val regNota = Regex("""\b([4-5][.,]\d{1,2})\b""")
        val preco = regPreco.find(textoCompleto)?.groupValues?.get(1)?.replace(",", ".")?.toDoubleOrNull()
        val km = regKm.find(textoCompleto)?.groupValues?.get(1)?.replace(",", ".")?.toDoubleOrNull()
        val nota = regNota.find(textoCompleto)?.groupValues?.get(1)?.replace(",", ".")?.toDoubleOrNull()
        if (preco == null || km == null || km == 0.0) return ResultadoAvaliacao(CorSemaforo.AMARELO, preco, km, null, nota, "Lendo dados da corrida...")
        val reaisKm = preco / km
        if (nota != null && nota < metaNota) return ResultadoAvaliacao(CorSemaforo.VERMELHO, preco, km, reaisKm, nota, "Passageiro com nota baixa ($nota)")
        if (reaisKm < metaKm) return ResultadoAvaliacao(CorSemaforo.VERMELHO, preco, km, reaisKm, nota, "R$ ${String.format("%.2f", reaisKm)}/km (Abaixo da meta)")
        return ResultadoAvaliacao(CorSemaforo.VERDE, preco, km, reaisKm, nota, "R$ ${String.format("%.2f", reaisKm)}/km (Boa corrida!)")
    }
}
