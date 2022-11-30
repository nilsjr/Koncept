package de.nilsdruyen.koncept.dogs.test

import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import io.github.serpro69.kfaker.Faker

object DogFactory {

    fun buildList(count: Int = 3) = List(count) {
        dog {
            id = it
            name = "DogBreed $it"
        }
    }

    fun build() = dog {
        id = 1
        name = "DogBreed"
    }

    object Random {

        private val faker = Faker()

        fun buildList(count: Int = 3) = List(count) { build() }

        fun build() = Dog(BreedId(faker.random.nextInt()), faker.dog.name())
    }
}

fun dog(block: DogBuilder.() -> Unit) = DogBuilder().apply(block).build()

class DogBuilder {

    var id: Int = 0
    var name: String = ""

    fun build(): Dog = Dog(BreedId(id), name)
}