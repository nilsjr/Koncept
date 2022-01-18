package de.nilsdruyen.koncept.dogs

import io.github.serpro69.kfaker.Faker

object DogFactory {

    private val faker = Faker()

    fun buildList(count: Int = 3) = List(count) { build() }

    fun build() = Dog(faker.random.nextInt(), faker.dog.name())
}