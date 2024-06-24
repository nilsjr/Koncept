package de.nilsdruyen.koncept.dogs.remote

import de.nilsdruyen.koncept.dogs.remote.entities.MeasureWebEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DogMapperKtTest {

    @Test
    fun `map measure web entity to int range`() {
        val entity = MeasureWebEntity(metric = "1 - 3")

        val result = entity.parseMeasureValue()

        assertThat(result.first).isEqualTo(1)
        assertThat(result.last).isEqualTo(3)
    }

    @Test
    fun `map measure web entity to int range 2`() {
        val entity = MeasureWebEntity(metric = "2")

        val result = entity.parseMeasureValue()

        assertThat(result.first).isEqualTo(2)
        assertThat(result.last).isEqualTo(2)
    }
}
