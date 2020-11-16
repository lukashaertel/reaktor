package eu.metatools.reaktor.gdx.internals

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.maltaisn.msdfgdx.MsdfShader
import com.maltaisn.msdfgdx.widget.MsdfLabel
import eu.metatools.reaktor.gdx.utils.hidden


private val msdfLabelShader = hidden<MsdfLabel, MsdfShader>("shader")
private val msdfLabelSkin = hidden<MsdfLabel, Skin>("skin")
private val msdfLabelWrap = hidden<Label, Boolean>("wrap")

var MsdfLabel.extShader
    get() = msdfLabelShader(this)
    set(value) = msdfLabelShader(this, value)

var MsdfLabel.extSkin
    get() = msdfLabelSkin(this)
    set(value) = msdfLabelSkin(this, value)

var MsdfLabel.extWrap
    get() = msdfLabelWrap(this)
    set(value) = msdfLabelWrap(this, value)