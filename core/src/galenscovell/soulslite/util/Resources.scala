package galenscovell.soulslite.util

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.graphics.g2d.freetype._
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable


object Resources {
  val assetManager: AssetManager = new AssetManager
  var atlas: TextureAtlas = _

  var labelTinyStyle, labelSmallStyle, labelMediumStyle,
  labelLargeStyle, labelXLargeStyle: LabelStyle = _

  var npGreen, npDarkBlue, npGray, npBlue, npDarkGray,
  greenButtonNp0, greenButtonNp1, blueButtonNp0, blueButtonNp1,
  npFontCursor, npTextFieldBg, npHorizontalBarFill, npHorizontalBarEmpty,
  npVerticalBarFill, npVerticalBarEmpty: NinePatchDrawable = _

  var buttonMenuStyle, buttonMapStyle0, buttonMapStyle1, buttonMapStyle2,
  buttonEventStyle, toggleButtonStyle, greenButtonStyle, blueButtonStyle: TextButtonStyle = _


  def load(): Unit = {
    assetManager.load("atlas/atlas.pack", classOf[TextureAtlas])
    val resolver: FileHandleResolver = new InternalFileHandleResolver
    assetManager.setLoader(classOf[FreeTypeFontGenerator], new FreeTypeFontGeneratorLoader(resolver))
    assetManager.setLoader(classOf[BitmapFont], ".ttf", new FreetypeFontLoader(resolver))

    generateFont("ui/Terminus.ttf", 14, 0, Color.WHITE, Color.BLACK, "tinyFont.ttf")
    generateFont("ui/Terminus.ttf", 16, 0, Color.WHITE, Color.BLACK, "smallFont.ttf")
    generateFont("ui/Terminus.ttf", 18, 0, Color.WHITE, Color.BLACK, "mediumFont.ttf")
    generateFont("ui/Terminus.ttf", 22, 0, Color.WHITE, Color.BLACK, "largeFont.ttf")
    generateFont("ui/cubeOne.ttf", 48, 0, Color.TEAL, Color.BLACK, "xLargeFont.ttf")
  }

  def done(): Unit = {
    atlas = assetManager.get("atlas/atlas.pack", classOf[TextureAtlas])
    loadNinepatches()
    loadLabelStyles()
    loadButtonStyles()
    loadSprites()
    loadAnimations()
    loadTextField()
    loadProgressBars()
  }

  def dispose(): Unit = {
    assetManager.dispose()
    atlas.dispose()
  }


  /*********************************
    * Font and Resource Generation *
    ********************************/
  private def generateFont(fontName: String, size: Int, borderWidth: Int, fontColor: Color, borderColor: Color, outName: String): Unit = {
    val params: FreetypeFontLoader.FreeTypeFontLoaderParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter
    params.fontFileName = fontName
    params.fontParameters.size = size
    params.fontParameters.borderWidth = borderWidth
    params.fontParameters.borderColor = borderColor
    params.fontParameters.color = fontColor
    params.fontParameters.magFilter = TextureFilter.Linear
    params.fontParameters.minFilter = TextureFilter.Linear
    assetManager.load(outName, classOf[BitmapFont], params)
  }

  private def loadNinepatches(): Unit = {
    npGreen = new NinePatchDrawable(atlas.createPatch("test-np-0"))
    npDarkBlue = new NinePatchDrawable(atlas.createPatch("test-np-1"))
    npGray = new NinePatchDrawable(atlas.createPatch("test-np-2"))
    npBlue = new NinePatchDrawable(atlas.createPatch("test-np-3"))
    npDarkGray = new NinePatchDrawable(atlas.createPatch("test-np-4"))

    greenButtonNp0 = new NinePatchDrawable(atlas.createPatch("green_button0_np"))
    greenButtonNp1 = new NinePatchDrawable(atlas.createPatch("green_button1_np"))
    blueButtonNp0 = new NinePatchDrawable(atlas.createPatch("blue_button0_np"))
    blueButtonNp1 = new NinePatchDrawable(atlas.createPatch("blue_button1_np"))

    npFontCursor = new NinePatchDrawable(atlas.createPatch("font-cursor-np"))
    npTextFieldBg = new NinePatchDrawable(atlas.createPatch("text-field-bg-np"))

    npHorizontalBarFill = new NinePatchDrawable(atlas.createPatch("horizontal-bar-fill-np"))
    npHorizontalBarEmpty = new NinePatchDrawable(atlas.createPatch("horizontal-bar-empty-np"))
    npVerticalBarFill = new NinePatchDrawable(atlas.createPatch("vertical-bar-fill-np"))
    npVerticalBarEmpty = new NinePatchDrawable(atlas.createPatch("vertical-bar-empty-np"))
  }

  private def loadLabelStyles(): Unit = {
    labelTinyStyle = new LabelStyle(assetManager.get("tinyFont.ttf", classOf[BitmapFont]), Color.WHITE)
    labelSmallStyle = new LabelStyle(assetManager.get("smallFont.ttf", classOf[BitmapFont]), Color.WHITE)
    labelMediumStyle = new LabelStyle(assetManager.get("mediumFont.ttf", classOf[BitmapFont]), Color.WHITE)
    labelLargeStyle = new LabelStyle(assetManager.get("largeFont.ttf", classOf[BitmapFont]), Color.WHITE)
    labelXLargeStyle = new LabelStyle(assetManager.get("xLargeFont.ttf", classOf[BitmapFont]), Color.WHITE)
  }

  private def loadButtonStyles(): Unit = {
    buttonMenuStyle = new TextButtonStyle(npDarkGray, npBlue, npDarkGray, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    buttonMenuStyle.pressedOffsetY = -2
    buttonMapStyle0 = new TextButtonStyle(npDarkBlue, npDarkGray, npDarkGray, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    buttonMapStyle0.pressedOffsetY = -2
    buttonMapStyle1 = new TextButtonStyle(npDarkBlue, npBlue, npBlue, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    buttonMapStyle1.pressedOffsetY = -2
    buttonMapStyle2 = new TextButtonStyle(npDarkBlue, npGray, npGray, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    buttonMapStyle2.pressedOffsetY = -2
    buttonEventStyle = new TextButtonStyle(npDarkBlue, npGray, npGray, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    buttonEventStyle.pressedOffsetY = -2
    toggleButtonStyle = new TextButtonStyle(npDarkGray, npBlue, npBlue, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    toggleButtonStyle.pressedOffsetY = -2
    greenButtonStyle = new TextButtonStyle(greenButtonNp0, greenButtonNp1, greenButtonNp0, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    //    greenButtonStyle.downFontColor = new Color(0.0f, 0.7f, 0.41f, 1.0f)
    //    greenButtonStyle.checkedFontColor = Color.WHITE
    greenButtonStyle.pressedOffsetY = -1
    blueButtonStyle = new TextButtonStyle(blueButtonNp0, blueButtonNp1, blueButtonNp0, assetManager.get("mediumFont.ttf", classOf[BitmapFont]))
    //    blueButtonStyle.downFontColor = new Color(0.0f, 0.7f, 0.41f, 1.0f)
    //    blueButtonStyle.checkedFontColor = Color.WHITE
    blueButtonStyle.pressedOffsetY = -1
  }

  private def loadSprites(): Unit = {

  }

  private def loadAnimations(): Unit = {

  }

  private def loadTextField(): Unit = {

  }

  private def loadProgressBars(): Unit = {
  }
}
