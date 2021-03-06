package eu.metatools.reaktor.gdx.test

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.Container
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.*
import com.badlogic.gdx.utils.viewport.*
import com.maltaisn.msdfgdx.FontStyle
import com.maltaisn.msdfgdx.MsdfFont
import com.maltaisn.msdfgdx.MsdfShader
import eu.metatools.reaktor.ex.*
import eu.metatools.reaktor.gdx.*
import eu.metatools.reaktor.gdx.data.ExtentValues
import eu.metatools.reaktor.gdx.data.Extents
import eu.metatools.reaktor.gdx.shapes.*
import eu.metatools.reaktor.gdx.utils.*
import eu.metatools.reaktor.reconcileNode
import kotlin.properties.Delegates

/**
 * If [isIn], animates with [setIn] over the given [time]. Otherwise, uses [setOut]. The value at the point of change
 * is captured with the given [capture] function. The [owner] manages the animation action. The animation functions
 * receive the captured initial value and the progress between zero and one.
 * @param isIn True if animation target is in.
 * @param owner The owner of the actions used for animations. If null, animation is skipped for now.
 * @param time The time in milliseconds.
 * @param capture The initial value capturing function.
 * @param setIn Animation in function.
 * @param setOut Animation out function.
 */
fun <T> useAnimationEffect(
    isIn: Boolean, owner: Actor?, time: Int, capture: () -> T,
    setIn: (T, Float) -> Unit,
    setOut: (T, Float) -> Unit,
) = useEffect(isIn to owner) {
    if (owner == null)
        return@useEffect

    // Mark start time and animation source value.
    val startTime = System.currentTimeMillis()
    val captured = capture()

    if (isIn)
        owner.actions.add(object : Action() {
            override fun act(delta: Float): Boolean {
                val progress = (System.currentTimeMillis() - startTime) / time.toFloat()
                setIn(captured, progress)
                return 1.0 <= progress
            }
        })
    else
        owner.actions.add(object : Action() {
            override fun act(delta: Float): Boolean {
                val progress = (System.currentTimeMillis() - startTime) / time.toFloat()
                setOut(captured, progress)
                return 1.0 <= progress
            }
        })
}

data class State(val windowVisible: Boolean, val resa: Int = 0)

class UISimpleTest : InputAdapter(), ApplicationListener {
    private val scalingFactor = 1.5f

    private val commonDimension = 8f

    private lateinit var stage: Stage
    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont
    private lateinit var white: TextureRegionDrawable
    private lateinit var whiteBorder: Drawable
    private lateinit var backgroundDrawable: Drawable
    private lateinit var progressRedDrawable: Drawable
    private lateinit var progressBlueDrawable: Drawable
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
    var state: State by Delegates.observable(State(windowVisible = false)) { _, old, new ->
        if (old != new)
            root()
    }

    val viewport by lazy {
        ScreenViewport().also { it.unitsPerPixel = 1f / (Gdx.graphics.density * scalingFactor) }
    }

    /**
     * State rendering function. Turns the state into the stage Virtual DOM.
     */
    val render = component { state: State ->
        var valueA by useState(0)
        var valueB by useState(0)

        useEffect(valueA to valueB) {
//            if (valueA > 0 && valueA == valueB) {
//                this@UISimpleTest.state = this@UISimpleTest.state.copy(windowVisible = false)
//            }
        }

        stage(viewport = viewport) {
            image(drawable = backgroundDrawable, fillParent = true)
            table(fillParent = true) {
                // TODO: Cells is needed here, otherwise state in filler is made twice.
                topBar(state, { valueA++ }, { valueB++ })
                filler(valueA, valueB)
                bottomBar(state, if (valueA < valueB) mapOf("A" to valueA, "B" to valueB) else
                    mapOf("B" to valueB, "A" to valueA))

            }

            // Add window if it is visible.
            if (state.windowVisible) {
                window(
                    title = "Title",
                    movable = true,
                    resizable = true,
                    style = WindowStyle(font, Color.WHITE, whiteBorder),
                    touchable = Touchable.enabled,
                    pad = ExtentValues(40f.px, 10f.px, 10f.px, 10f.px),
                    width = 500f,
                    height = 200f) {
                    cell(row = 0, column = 0, expandX = 1, expandY = 0) {
                        button("Increase A", TextureRegionDrawable(iconsAtlas["research"]!!)) { valueA++ }
                    }
                    cell(row = 0, column = 1, expandX = 1, expandY = 0) {
                        button("Increase B", TextureRegionDrawable(iconsAtlas["gold"]!!)) { valueB++ }
                    }
                    cell(row = 0, column = 2, expandX = 1, expandY = 0) {
                        button("Close", null) {
                            this@UISimpleTest.state = state.copy(windowVisible = false)
                        }
                    }
                }
            }
        }
    }

    private val borderedPad = ExtentValues(commonDimension, commonDimension)

    private fun bordered(
        ref: (Container<Actor>) -> Unit = {},
        listeners: List<EventListener> = listOf(),
        content: () -> Unit,
    ) = container(ref = ref, listeners = listeners, pad = borderedPad) {
        container(pad = borderedPad, background = whiteBorder) {
            content()
        }
    }

    private val button = component { label: String, img: Drawable?, clicked: () -> Unit ->
        // Base mutable states.
        var root by useState<Actor?>(null)
        var over by useState(false)

        // Animated state.
        val style by useState { FontStyle(fontWhite).setSize(18f) }

        // Animate using effect based on "over" state. Interpolate weight from captured value to target.
        useAnimationEffect(over, root, 350, { style.weight },
            { w, t -> style.weight = MathUtils.lerp(w, 0.2f, t) },
            { w, t -> style.weight = MathUtils.lerp(w, 0.0f, t) })

        // Listen to different input events.
        val listener = inputListener {
            when {
                it.type == InputEvent.Type.enter && it.pointer < 0 -> true.also { over = true }
                it.type == InputEvent.Type.exit && it.pointer < 0 -> true.also { over = false }
                it.type == InputEvent.Type.touchDown -> true.also { clicked() }
                else -> false
            }
        }

        // Return bordered horizontal group with optional image and button.
        bordered({ root = it }, listOf(listener)) {
            horizontalGroup(space = commonDimension) {
                if (img != null)
                    image(drawable = img)

                msdfLabel(label,
                    shader = msdfShader,
                    font = msdfFont,
                    fontStyle = style)
            }
        }
    }

    private val progress = component { filled: Drawable, open: Drawable, progress: Float ->
        image(object : BaseDrawable() {
            override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
                val mid = progress * width
                if (mid > 0f)
                    filled.draw(batch, x, y, mid, height)
                if (mid < width)
                    open.draw(batch, x + mid, y, width - mid, height)
            }
        })
    }

    private val debugLabel = component { label: Any, value: Any ->
        msdfLabel(
            text = "$label: $value",
            shader = msdfShader, font = msdfFont,
            fontStyle = FontStyle(fontWhite).setColor(Color.RED)
        )
    }

    private val bottomBar = component { state: State, extra: Map<Any, Any> ->
        cell(row = 2, expandX = 1, fillX = 1f, fillY = 1f) {
            container(background = whiteBorder, fillX = 1f) {
                horizontalGroup(pad = Extents(8f, 2f), space = 20f) {
                    msdfLabel(
                        text = state.toString(),
                        shader = msdfShader, font = msdfFont,
                        fontStyle = FontStyle(fontWhite)
                    )

                    // Iterating in a loop. Key must be set because the location of the call will be the same, but
                    // the arguments are actually different.
                    for ((k, v) in extra)
                        debugLabel(key = k, k, v)
                }
            }
        }
    }

    private val filler = component { valueA: Number, valueB: Number ->
        val num = minOf(valueA.toFloat(), valueB.toFloat())
        val den = maxOf(valueA.toFloat(), valueB.toFloat())
        val ratio = if (den == 0f) 0f else num / den

        cell(row = 1, expandY = 1) {
            container(minWidth = 200.px,
                minHeight = (commonDimension * 2f).px,
                background = whiteBorder,
                pad = ExtentValues(commonDimension)) {
                progress(progressRedDrawable, progressBlueDrawable, ratio)
            }
        }
    }

    private val topBar = component { state: State, onMenu: () -> Unit, onGreatPeople: () -> Unit ->
        cell(row = 0, expandX = 1, fillX = 1f) {
            container(fillX = 1f) {
                horizontalGroup(space = -commonDimension) {
                    button("Menu", iconsAtlas["checkbox"].asDrawable()) {
                        onMenu()
                    }
                    button("Great people", iconsAtlas["great_people"].asDrawable()) {
                        onGreatPeople()
                    }
                    button("Things and stuff", null) {
                        this@UISimpleTest.state = state.copy(windowVisible = true)
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

        backgroundDrawable = RectDrawable(Fill, gradientBottom("#4e8771".hex, "#325e53".hex))

        val wc = Corners.all except Corners.topRight
        whiteBorder = LayerDrawable(
            RectRoundedDrawable(Fill, commonDimension, "#ffffff20".hex, corners = wc),
            RectRoundedDrawable(Line, commonDimension, "#ffffff70".hex, corners = wc),
//            object : PainterDrawable(Fill) {
//                val colorField = verticalGradient("#4589cf".hex.brighter, "#4589cf".hex.darker)
//                override fun draw(painter: Painter, x: Float, y: Float, width: Float, height: Float) {
//                    val colorAt = colorField(x, y, width, height)
//
//                    painter.next(x, y + height / 2f, colorAt)
//                    painter.next(x + width / 2f, y + height, colorAt)
//                    painter.next(x + width, y + height / 2f, colorAt)
//                    painter.next(x + width / 2f, y, colorAt)
//                }
//            }
        )

        val rc = Corners.bottomLeft and Corners.topLeft
        val bc = Corners.bottomRight and Corners.topRight
        progressRedDrawable = LayerDrawable(
            RectRoundedDrawable(Fill, commonDimension, gradientLeft("#ed1234".hex.darker, "#fe2345".hex), rc),
            RectRoundedDrawable(Line, commonDimension, "#560102".hex, rc))

        progressBlueDrawable = LayerDrawable(
            RectRoundedDrawable(Fill, commonDimension, gradientLeft("#1234ed".hex.darker, "#2345fe".hex), bc),
            RectRoundedDrawable(Line, commonDimension, "#010256".hex, bc))

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
        stage.act(Gdx.graphics.deltaTime)
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
    LwjglApplication(UISimpleTest(), LwjglApplicationConfiguration().apply {
        samples = 16
        width = 1920
        height = 1080
    })
}