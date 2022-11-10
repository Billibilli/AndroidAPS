package info.nightscout.implementation.queue.commands

import dagger.android.HasAndroidInjector
import info.nightscout.androidaps.interfaces.ActivePlugin
import info.nightscout.androidaps.queue.commands.Command
import info.nightscout.implementation.R
import info.nightscout.interfaces.pump.Dana
import info.nightscout.interfaces.pump.Diaconn
import info.nightscout.interfaces.queue.Callback
import info.nightscout.rx.logging.LTag
import javax.inject.Inject

class CommandLoadHistory(
    injector: HasAndroidInjector,
    private val type: Byte,
    callback: Callback?
) : Command(injector, CommandType.LOAD_HISTORY, callback) {

    @Inject lateinit var activePlugin: ActivePlugin

    override fun execute() {
        val pump = activePlugin.activePump
        if (pump is Dana) {
            val danaPump = pump as Dana
            val r = danaPump.loadHistory(type)
            aapsLogger.debug(LTag.PUMPQUEUE, "Result success: " + r.success + " enacted: " + r.enacted)
            callback?.result(r)?.run()
        }

        if (pump is Diaconn) {
            val diaconnG8Pump = pump as Diaconn
            val r = diaconnG8Pump.loadHistory()
            aapsLogger.debug(LTag.PUMPQUEUE, "Result success: " + r.success + " enacted: " + r.enacted)
            callback?.result(r)?.run()
        }
    }

    override fun status(): String = rh.gs(R.string.load_history, type.toInt())

    override fun log(): String = "LOAD HISTORY $type"
}