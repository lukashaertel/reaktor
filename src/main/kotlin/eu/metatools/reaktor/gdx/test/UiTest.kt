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
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.MsdfFont
import com.maltaisn.msdfgdx.MsdfShader
import eu.metatools.reaktor.ex.*
import eu.metatools.reaktor.gdx.*
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.data.Extents
import eu.metatools.reaktor.gdx.utils.eventListener
import eu.metatools.reaktor.gdx.utils.get
import eu.metatools.reaktor.gdx.utils.hex
import eu.metatools.reaktor.gdx.utils.px
import eu.metatools.reaktor.reconcileNode
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
     * Target node for receiving reconcile with actual.
     */
    var stageNode by reconcileNode {
        stage = it as Stage
        Gdx.input.inputProcessor = it
    }

    /**
     * Generator handle. On yield, sets the reconcile node.
     */
    val root = hostRoot({ stageNode = it }) {
        render(state)
    }

    /**
     * Current state that the UI is based on. On change, invokes the wrapper with the result of rendering.
     */
    var state: State by Delegates.observable(State(windowVisible = true, variant = false)) { _, old, new ->
        if (old != new)
            root()
    }

    /**
     * Yield component with the given icon (referring to one of the icons in the icons atlas), the text and the color.
     */
    val yield = component { icon: String, text: String, color: Color ->
        // Will be assigned to the actor that handles the animation action.
        var actionOwner by useState<HorizontalGroup?>(null)

        // True if over, false if not over.
        var over by useState(false)

        // Initial references. Color will be modified directly and inject into LibGDX by reference.
        val colorCurrent by useState(Color(color))
        val listener by useState {
            eventListener<InputEvent> {
                // When entering and leaving, but not on press, handle and carry over state.
                when (it.type) {
                    InputEvent.Type.enter -> if (it.pointer < 0) true.apply { over = true } else false
                    InputEvent.Type.exit -> if (it.pointer < 0) true.apply { over = false } else false
                    else -> false
                }
            }
        }

        // Change on action owner and mouse over.
        useEffect(listOf(over, actionOwner)) {
            // Get action owner, if not present, ignore.
            val target = actionOwner ?: return@useEffect

            // Mark start time and animation source color.
            val startTime = System.currentTimeMillis()
            val colorSource = colorCurrent.cpy()

            // Add action.
            target.actions.add(object : Action() {
                override fun act(delta: Float): Boolean {
                    // Animate over 200ms.
                    val progress = 5.0f * (System.currentTimeMillis() - startTime) / 1000.0f

                    // Animate in the desired direction.
                    if (over)
                        colorCurrent.set(colorSource.cpy().lerp(color.cpy().mul(0.5f, 0.5f, 0.5f, 1f), progress))
                    else
                        colorCurrent.set(colorSource.cpy().lerp(color, progress))

                    // Return after finished.
                    return 1.0 <= progress
                }
            })
        }


        VHorizontalGroup(
            ref = { actionOwner = it },
            align = Align.center,
            listeners = listOf(listener),
            touchable = Touchable.enabled) {
            +VImage(drawable = TextureRegionDrawable(iconsAtlas[icon] ?: throw NoSuchElementException(icon)))

            +VMsdfLabel(
                text = text, shader = msdfShader, font = msdfFont,
                fontStyle = FontStyle(fontWhite)
                    .setColor(colorCurrent)
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
        var valueA by useState(0)
        var valueB by useState(0)

        useEffect(valueA to valueB) {
            if (valueA > 0 && valueA == valueB) {
                this@UISimpleTest.state = this@UISimpleTest.state.copy(windowVisible = false)
            }
        }

        VStage {
            +VTable(fillParent = true) {
                // TODO: Cells is needed here, otherwise state in filler is made twice.
                cells {
                    +yieldsAndResRow(state)
                    +filler()
                    +debugRow(state, mapOf("A" to valueA, "B" to valueB))
                }
            }

            // Add window if it is visible.
            if (state.windowVisible) {

                val sva = eventListener<InputEvent> {
                    if (it.type == InputEvent.Type.touchDown) {
                        valueA = valueA + 1
                        true
                    } else false
                }
                val svb = eventListener<InputEvent> {
                    if (it.type == InputEvent.Type.touchDown) {
                        valueB = valueB + 1
                        true
                    } else false
                }
                val svc = eventListener<InputEvent> {
                    if (it.type == InputEvent.Type.touchDown) {
                        this@UISimpleTest.state = state.copy(windowVisible = false)
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
                    cells {
                        +VCell(row = 0, column = 0, expandX = 1, expandY = 1) {
                            +VMsdfLabel("Set variant 1", shader = msdfShader, font = msdfFont, fontStyle = fontWhite,
                                listeners = listOf(sva))
                        }
                        +VCell(row = 0, column = 1, expandX = 1, expandY = 1) {
                            +VMsdfLabel("Set variant 2", shader = msdfShader, font = msdfFont, fontStyle = fontWhite,
                                listeners = listOf(svb))
                        }
                        +VCell(row = 0, column = 2, expandX = 1, expandY = 1) {
                            +VMsdfLabel("Close", shader = msdfShader, font = msdfFont, fontStyle = fontWhite,
                                listeners = listOf(svc))
                        }
                    }
                }
            }
        }
    }


    private val debugRow = component { state: State, extra: Map<Any, Any> ->
        VCell(row = 2, expandX = 1, fillX = 1f, fillY = 1f) {
            +VContainer(background = white.tint("#00000040".hex), fillX = 1f) {
                actor {
                    +VHorizontalGroup(pad = Extents(8f, 2f), space = 20f) {
                        +VMsdfLabel(
                            text = state.toString(),
                            shader = msdfShader, font = msdfFont,
                            fontStyle = FontStyle(fontWhite)
                        )

                        for ((k, v) in extra)
                            +VMsdfLabel(
                                text = "$k: $v",
                                shader = msdfShader, font = msdfFont,
                                fontStyle = FontStyle(fontWhite).setColor(Color.GOLDENROD)
                            )
                    }
                }
            }
        }
    }

    private val filler = component { ->
        var selected by useState("A")

        useEffect(selected) {
            if (selected == "1")
                selected = "A"
        }

        // Think i spotted a bug going on with selected, but cannot reproduce.
        VCell(row = 1, expandY = 1) {
            +VContainer(pad = ExtentValues(10f), background = white.tint("#00000040".hex)) {
                actor {
                    +VList(listStyle,
                        items = listOf("A", "B", "C", "1", "2", "3"),
                        selected = selected,
                        width = 100f, height = 100f, listeners = listOf(
                            eventListener<ChangeListener.ChangeEvent> {
                                selected = it.listSelected as String
                                true
                            }
                        ))
                }
            }

        }
    }


    private val yieldsAndResRow = component { state: State ->
        VCell(row = 0, expandX = 1, fillX = 1f, fillY = 1f) {
            +VContainer(background = white.tint("#00000080".hex), fillX = 1f) {
                actor {
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
    }

    override fun create() {
        stage = Stage()
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
        root()
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