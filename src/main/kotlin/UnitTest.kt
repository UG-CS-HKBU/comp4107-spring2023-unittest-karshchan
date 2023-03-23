import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.random.Random


class UnitTest {
    var monarchHero = CaoCao()
    val heros = mutableListOf<Hero>()
    @Test
    fun testCaoDodgeAttack() {
        for (i in 0..6)
            heros.add(NoneMonarchFactory.createRandomHero())
        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked() {
        if (heros.isEmpty()) {
            for (i in 0..6)
                heros.add(NoneMonarchFactory.createRandomHero())
        }
        for (hero in heros) {
            val spy = object: Hero(MinisterRole()) {
                override val name = hero.name
                override fun beingAttacked() {
                    hero.beingAttacked()
                    assertTrue(hero.hp >= 0)
                }
            }

            for (i in 1..10) {
                hero.beingAttacked()
                assertTrue(hero.hp >= 0)
            }
        }
    }

    @Test
    fun testDiscardCards() {
        val dummy = DummyRole("")
        val hero = ZhangFei(dummy)
        hero.discardCards()
    }

}

object FakeNonmonarchFactory:GameObjectFactory {
    override fun getRandomRole(): Role {
        return MinisterRole()
    }

    override fun createRandomHero(): Hero {
        var hero = when (Random.nextInt(3)) {
            0 -> SimaYi(getRandomRole())
            1 -> XuChu(getRandomRole())
            else -> XiaHouyuan(getRandomRole())
        }

        if (fakeMonarchHero != null) {
            if (fakeMonarchHero is CaoCao) {
                val cao = fakeMonarchHero as CaoCao
                if (cao.helper == null)
                    cao.helper = hero as WeiHero
                else {
                    var handler: Handler? = cao.helper as Handler

                    while (handler!!.hasNext())
                        handler = handler.getNext()
                    handler.setNext(hero as Handler)
                }
            }
        }

        return hero
    }

}

object FakeMonarchFactory : GameObjectFactory {
    override fun getRandomRole(): Role {
        TODO("Not yet implemented")
    }

    override fun createRandomHero(): Hero {
        return CaoCao()
    }

}

var fakeMonarchHero : Hero? = null
val fakeHeroes = mutableListOf<Hero>()

class CaoCaoUnitTest {
    @Test
    fun testCaoDodgeAttack() {
        fakeMonarchHero = FakeMonarchFactory.createRandomHero()
        fakeHeroes.add(fakeMonarchHero!!)
        for (i in 0..2) {
            var hero = FakeNonmonarchFactory.createRandomHero()
            hero.index = fakeHeroes.size;
            fakeHeroes.add(hero)
        }

        for (hero in fakeHeroes) {
            if (hero is CaoCao) {
                hero.beingAttacked()
                assertTrue(hero.dodgeAttack())
            } else {
                hero.beingAttacked()
            }
        }
    }
}

class DummyRole(override val roleTitle: String) : Role{
    override fun getEnemy(): String {
        TODO("Not yet implemented")
    }

}