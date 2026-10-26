package ru.fefu.dvfu.hero

import android.app.Application

class HeroApp : Application() {
    val repository = GameRepository()
}
