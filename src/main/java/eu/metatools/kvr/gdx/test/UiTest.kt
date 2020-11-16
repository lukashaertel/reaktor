package eu.metatools.kvr.gdx.test

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.MsdfFont
import com.maltaisn.msdfgdx.MsdfShader
import eu.metatools.kvr.gdx.*
import eu.metatools.kvr.gdx.data.Extents
import eu.metatools.kvr.gdx.utils.get
import eu.metatools.kvr.gdx.utils.hex
import eu.metatools.kvr.gdx.utils.windowBehavior
import eu.metatools.kvr.reconcileWrapper
import kotlin.properties.Delegates


data class State(val windowVisible: Boolean)

class UISimpleTest : InputAdapter(), ApplicationListener {
    lateinit var stage: Stage
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    lateinit var white: TextureRegionDrawable
    lateinit var empty: BaseDrawable

    lateinit var windowStyle: WindowStyle
    lateinit var labelStyle: LabelStyle
    lateinit var listStyle: ListStyle
    lateinit var scrollPaneStyle: ScrollPaneStyle

    lateinit var msdfShader: MsdfShader
    lateinit var msdfFont: MsdfFont

    lateinit var iconsAtlas: TextureAtlas

    val wrapper = reconcileWrapper {
        stage = it as Stage
        Gdx.input.inputProcessor = it
    }

    var state: State by Delegates.observable(State(true)) { _, _, newValue ->
        wrapper(app(newValue))
    }

    val fontWhite = FontStyle()
        .setColor("#ffffff".hex)
        .setShadowOffset(Vector2(1f, 1f))
        .setShadowColor("#00000060".hex)
        .setSize(24f)

    val vectorUnit = Vector2(1f, 1f)

    val yield = component<String, String, Color, VHorizontalGroup> { icon, text, color ->
        horizontalGroup(align = Align.center) {
            image(drawable = TextureRegionDrawable(iconsAtlas[icon] ?: throw NoSuchElementException(icon)))

            msdfLabel(
                text = text, shader = msdfShader, font = msdfFont,
                fontStyle = FontStyle(fontWhite)
                    .setColor(color)
                    .setSize(20f)
                    .setShadowColor("#00000040".hex)
                    .setShadowOffset(vectorUnit)
            )
        }
    }
    val res = component<String, Int, VHorizontalGroup> { icon: String, amount: Int ->
        horizontalGroup(align = Align.center) {
            msdfLabel(
                text = "$amount", shader = msdfShader, font = msdfFont,
                fontStyle = FontStyle(fontWhite)
                    .setColor(if (amount < 0) "#e62039".hex else "#65d861".hex)
                    .setSize(20f)
                    .setShadowColor("#00000040".hex)
                    .setShadowOffset(vectorUnit)
            )

            image(drawable = TextureRegionDrawable(iconsAtlas[icon] ?: throw NoSuchElementException(icon)))
        }
    }

    val app = component<State, VStage> { state: State ->
        stage {
            table(fillParent = true) {
                cell(row = 0, expandX = 1, fillX = 1f, fillY = 1f) {
                    container(background = white.tint("#00000080".hex), fillX = 1f) {
                        horizontalGroup(pad = Extents(8f, 2f), space = 16f) {
                            yield("research", "+213", "#41add3".hex)
                            yield("gold", "618 (+62)", "#ebe932".hex)
                            yield("trade_white", "4 / 5", "#fcfddd".hex)
                            yield("happiness_1", "2", "#65d861".hex)
                            yield("golden_age", "328/1050", "#ffffff".hex)
                            yield("culture", "493/830 (+26)", "#d423d6".hex)
                            yield("arrow_right", "+2", "#f7fadf".hex)
                            yield("peace", "104 (+21)", "#f7fadf".hex)

                            res("res_iron", 6)
                            res("res_horse", 16)
                            res("res_coal", -1)
                        }
                    }
                }

                cell(row = 1, expandY = 1) {
                    image(drawable = white)
                }

                cell(row = 2, expandX = 1, fillX = 1f, fillY = 1f) {
                    container(background = white.tint("#00000040".hex), fillX = 1f) {
                        horizontalGroup(pad = Extents(8f, 2f)) {
                            msdfLabel(
                                text = state.toString(),
                                shader = msdfShader, font = msdfFont,
                                fontStyle = FontStyle(fontWhite)
                            )
                        }
                    }
                }
            }

            if (state.windowVisible) {
                val move = windowBehavior(Extents(40f, 10f, 10f, 10f), isResizable = true, resizeBorder = 20f)
                table(
                    touchable = Touchable.enabled,
                    width = 400f,
                    height = 400f,
                    listeners = listOf(move),
                    background = white.tint("#00000040".hex)) {

                }
//                window(
//                    "Windig", windowStyle, width = 400f, height = 400f, movable = true, resizable = true,
//                    pad = VWindow.defaultPad.copy(top = 40.px)
//                ) {
//                    cell(0, 0, fillY = 1f) {
//                        msdfLabel(
//                            text = "Window content",
//                            shader = msdfShader, font = msdfFont,
//                            fontStyle = FontStyle(fontWhite))
//                    }
//                    cell(0, 1, fillX = 1f, fillY = 1f) {
//                        list(
//                            style = listStyle,
//                            listeners = listOf(hide),
//                            items = (0..10).map { "Item #$it" }.toList())
//                    }
//                }
            }
        }
    }

//    val appX = component { state: State ->
//        stage(debugAll = true) {
//            scrollPane(scrollPaneStyle, fillParent = true, forceScrollY = true) {
//                verticalGroup {
//                    repeat(10) { image(drawable = white.tint(if (it % 2 == 0) Color.BLACK else Color.YELLOW)) }
//                    header()
//                    selector(state.selected, state.selectedRight)
//                    repeat(10) { image(drawable = white.tint(if (it % 2 == 1) Color.BLACK else Color.YELLOW)) }
//                }
//            }
//        }
//    }

    override fun create() {
        batch = SpriteBatch()

        // Make font.
        font = BitmapFont()

        // Make white texture.
        white = TextureRegionDrawable(Texture(Pixmap(32, 32, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE)
            fill()
        }))

        // Make empty drawable.
        empty = BaseDrawable()

        windowStyle = WindowStyle(font, Color.WHITE, white.tint("#00000040".hex))
        labelStyle = LabelStyle(font, Color.BLACK)
        listStyle = ListStyle(font, Color.RED, Color.BLACK, empty)
        scrollPaneStyle = ScrollPaneStyle(white.tint(Color.GRAY),
            white.tint(Color.CYAN), white.tint(Color.BLUE),
            white.tint(Color.RED), white.tint(Color.MAROON))

        msdfShader = MsdfShader()
        msdfFont = MsdfFont(Gdx.files.internal("res/BarlowSemiCondensed-Regular.fnt"), 32f, 5f)
        iconsAtlas = TextureAtlas(Gdx.files.internal("res/gak.atlas"))

        // Initialize.
        wrapper(app(state))

//        // Create a table that fills the screen. Everything else will go inside this table.
//        val table = Table()
//        table.setFillParent(true)
//        stage.addActor(table)
//
//        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
//        val button = TextButton("Click me!", skin)
//        table.add(button)
//
//        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
//        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
//        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
//        // revert the checked state.
//        button.addListener(object : ChangeListener() {
//            fun changed(event: ChangeEvent?, actor: Actor?) {
//                println("Clicked! Is checked: " + button.isChecked)
//                button.setText("Good job!")
//            }
//        })
//
//        // Add an image actor. Have to set the size, belse it would be the size of the drawable (which is the 1x1 texture).
//        table.add(Image(skin!!.newDrawable("white", Color.RED))).size(64)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime.coerceAtMost(1 / 30f))
        stage.draw()
    }


    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun pause() = Unit

    override fun resume() = Unit
}

fun main() {
    LwjglApplication(UISimpleTest(), LwjglApplicationConfiguration())
}