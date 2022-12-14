import bot.bots.PonderingBot
import gui.GUIHumanPlayer
import gui.Gui
import model.Token
import model.procedure.Game

class Controller {
    companion object {
        val INIT_P1 = GUIHumanPlayer(Token.PLAYER_1)
        val INIT_P2 = PonderingBot(Token.PLAYER_2)
        const val INIT_WIDTH = 11
        const val INIT_HEIGHT = 8
    }

    private lateinit var game: Game
    private lateinit var gameThread: Thread
    private lateinit var gui: Gui

    fun startNewGame() {
        val p1 = if (!::gui.isInitialized || gui.p1Selected == null) INIT_P1 else gui.p1Selected!!
        val p2 = if (!::gui.isInitialized || gui.p2Selected == null) INIT_P2 else gui.p2Selected!!
        val width = if (!::gui.isInitialized || gui.widthSelected == -1) INIT_WIDTH else gui.widthSelected
        val height = if (!::gui.isInitialized || gui.heightSelected == -1) INIT_HEIGHT else gui.heightSelected
        game = Game(p1, p2, width, height, this)
        if (!this::gui.isInitialized) gui = Gui(this)
        gameThread = Thread { game.play() }
        gameThread.start()
    }

    fun stopCurrentGame() {
        if (this::gameThread.isInitialized) {
            if (gameThread.state == Thread.State.RUNNABLE) {
                if (this::game.isInitialized) {
                    game.running = false
                }
            }
        }
    }

    fun updateGui() {
        gui.update()
    }

    val board get() = game.board
    val currentPlayer get() = game.currentPlayer
}