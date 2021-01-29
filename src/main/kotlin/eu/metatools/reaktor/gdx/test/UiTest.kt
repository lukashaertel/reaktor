package eu.metatools.reaktor.gdx.test

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
import com.badlogic.gdx.scenes.scene2d.InputEvent
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
import eu.metatools.reaktor.gdx.*
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.data.Extents
import eu.metatools.reaktor.gdx.utils.eventListener
import eu.metatools.reaktor.gdx.utils.get
import eu.metatools.reaktor.gdx.utils.hex
import eu.metatools.reaktor.gdx.utils.px
import eu.metatools.reaktor.reconcileWrapper
import kotlin.properties.Delegates

data class State(val windowVisible: Boolean, val variant: Boolean, val resa: Int = 0)

class UISimpleTest : InputAdapter(), ApplicationListener {
    private lateinit var stage: Stage
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var white: TextureRegionDrawable
    private lateinit var empty: BaseDrawable
    private lateinit var windowStyle: WindowStyle
    private lateinit var labelStyle: LabelStyle
    private lateinit var listStyle: ListStyle
    private lateinit var scrollPaneStyle: ScrollPaneStyle
    private lateinit var msdfShader: MsdfShader
    private lateinit var msdfFont: MsdfFont
    private lateinit var iconsAtlas: TextureAtlas

    private val fontWhite: FontStyle = FontStyle()
        .setColor("#ffffff".hex)
        .setShadowOffset(Vector2(1f, 1f))
        .setShadowColor("#00000060".hex)
        .setSize(24f)

    private val vectorUnit = Vector2(1f, 1f)

    /**
     * When invoked with the new virtual DOM, invokes reconcile and sets the stage to the received argument.
     */
    val wrapper = reconcileWrapper {
        stage = it as Stage
        Gdx.input.inputProcessor = it
    }

    /**
     * Current state that the UI is based on. On change, invokes the wrapper with the result of rendering.
     */
    var state: State by Delegates.observable(State(windowVisible = true, variant = false)) { _, _, newValue ->
        wrapper(render(newValue))
    }


    /**
     * Yield component with the given icon (referring to one of the icons in the icons atlas), the text and the color.
     */
    val yield = component<String, String, Color, VHorizontalGroup> { icon, text, color ->
        VHorizontalGroup(align = Align.center) {
            +VImage(drawable = TextureRegionDrawable(iconsAtlas[icon] ?: throw NoSuchElementException(icon)))

            +VMsdfLabel(
                text = text, shader = msdfShader, font = msdfFont,
                fontStyle = FontStyle(fontWhite)
                    .setColor(color)
                    .setSize(20f)
                    .setShadowColor("#00000040".hex)
                    .setShadowOffset(vectorUnit)
            )
        }
    }

    /**
     * Resource, layout amount, then icon. If amount is negative, red is used as the color, otherwise green.
     */
    val res = component { icon: String, amount: Int ->
        VHorizontalGroup(align = Align.center) {
            +VMsdfLabel(
                text = "$amount", shader = msdfShader, font = msdfFont,
                fontStyle = FontStyle(fontWhite)
                    .setColor(if (amount < 0) "#e62039".hex else "#65d861".hex)
                    .setSize(20f)
                    .setShadowColor("#00000040".hex)
                    .setShadowOffset(vectorUnit)
            )

            +VImage(drawable = TextureRegionDrawable(iconsAtlas[icon] ?: throw NoSuchElementException(icon)))
        }
    }

    /**
     * State rendering function. Turns the state into the stage Virtual DOM.
     */
    val render = component { state: State ->
        VStage {
            +VTable(fillParent = true) {
                +yieldsAndResRow(state)
                +filler()
                +debugRow(state)
            }

            // Add window if it is visible.
            if (state.windowVisible) {
                val sva = eventListener<InputEvent> {
                    if (it.type == InputEvent.Type.touchDown) {
                        this@UISimpleTest.state = state.copy(variant = false, resa = state.resa.inc())
                        true
                    } else false
                }
                val svb = eventListener<InputEvent> {
                    if (it.type == InputEvent.Type.touchDown) {
                        this@UISimpleTest.state = state.copy(variant = true, resa = state.resa.inc())
                        true
                    } else false
                }
                +VWindow(
                    title = "Title",
                    movable = true,
                    resizable = true,
                    style = WindowStyle(font,
                        Color.WHITE,
                        white.tint(if (state.variant) "#ff000040".hex else "#00000040".hex)),
                    touchable = Touchable.enabled,
                    pad = ExtentValues(40f.px, 10f.px, 10f.px, 10f.px),
                    width = 400f,
                    height = 400f) {
                    +VCell(row = 0, column = 0, expandX = 1, expandY = 1) {
                        +VMsdfLabel("Set variant 1", shader = msdfShader, font = msdfFont, fontStyle = fontWhite,
                            listeners = listOf(sva))
                    }
                    +VCell(row = 0, column = 1, expandX = 1, expandY = 1) {
                        +VMsdfLabel("Set variant 2", shader = msdfShader, font = msdfFont, fontStyle = fontWhite,
                            listeners = listOf(svb))
                    }
                }
            }
        }
    }

    private val debugRow = component { state: State ->
        VCell(row = 2, expandX = 1, fillX = 1f, fillY = 1f) {
            +VContainer(background = white.tint("#00000040".hex), fillX = 1f) {
                +VHorizontalGroup(pad = Extents(8f, 2f)) {
                    +VMsdfLabel(
                        text = state.toString(),
                        shader = msdfShader, font = msdfFont,
                        fontStyle = FontStyle(fontWhite)
                    )
                }
            }
        }
    }

    private val filler = component { ->
        VCell(row = 1, expandY = 1)
    }


    private val yieldsAndResRow = { state: State ->
        VCell(row = 0, expandX = 1, fillX = 1f, fillY = 1f) {
            +VContainer(background = white.tint("#00000080".hex), fillX = 1f) {
                +VHorizontalGroup(pad = Extents(8f, 2f), space = 16f) {
                    +yield("research", "+213", "#41add3".hex)
                    +yield("gold", "618 (+62)", "#ebe932".hex)
                    +yield("trade_white", "4 / 5", "#fcfddd".hex)
                    +yield("happiness_1", state.resa.toString(), "#65d861".hex)
                    +yield("golden_age", "328/1050", "#ffffff".hex)
                    +yield("culture", "493/830 (+26)", "#d423d6".hex)
                    +yield("arrow_right", "+2", "#f7fadf".hex)
                    +yield("peace", "104 (+21)", "#f7fadf".hex)

                    +res("res_iron", 6)
                    +res("res_horse", 16)
                    +res("res_coal", -1)
                }
            }
        }
    }

    override fun create() {
        batch = SpriteBatch()

        // Make font.
        font = BitmapFont()

        // Make white texture.
        white = TextureRegionDrawable(Texture(Pixmap(32, 32, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE)
            fill()
        }))

        // Make empty context dependent resources.
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
        wrapper(render(state))
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