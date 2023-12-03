package com.astarconcepts.brandbanao.utils;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class FilterAdjuster {
    private final Adjuster<? extends GPUImageFilter> adjuster;

    public FilterAdjuster(GPUImageFilter gPUImageFilter) {
        if (gPUImageFilter instanceof GPUImageSharpenFilter) {
            this.adjuster = new SharpnessAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageSepiaFilter) {
            this.adjuster = new SepiaAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageContrastFilter) {
            this.adjuster = new ContrastAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageGammaFilter) {
            this.adjuster = new GammaAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageBrightnessFilter) {
            this.adjuster = new BrightnessAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageSobelEdgeDetection) {
            this.adjuster = new SobelAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageEmbossFilter) {
            this.adjuster = new EmbossAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImage3x3TextureSamplingFilter) {
            this.adjuster = new GPU3x3TextureAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageHueFilter) {
            this.adjuster = new HueAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImagePosterizeFilter) {
            this.adjuster = new PosterizeAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImagePixelationFilter) {
            this.adjuster = new PixelationAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageSaturationFilter) {
            this.adjuster = new SaturationAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageExposureFilter) {
            this.adjuster = new ExposureAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageHighlightShadowFilter) {
            this.adjuster = new HighlightShadowAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageMonochromeFilter) {
            this.adjuster = new MonochromeAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageOpacityFilter) {
            this.adjuster = new OpacityAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageRGBFilter) {
            this.adjuster = new RGBAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageWhiteBalanceFilter) {
            this.adjuster = new WhiteBalanceAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageVignetteFilter) {
            this.adjuster = new VignetteAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageDissolveBlendFilter) {
            this.adjuster = new DissolveBlendAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageGaussianBlurFilter) {
            this.adjuster = new GaussianBlurAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageCrosshatchFilter) {
            this.adjuster = new CrosshatchBlurAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageBulgeDistortionFilter) {
            this.adjuster = new BulgeDistortionAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageGlassSphereFilter) {
            this.adjuster = new GlassSphereAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageHazeFilter) {
            this.adjuster = new HazeAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageSphereRefractionFilter) {
            this.adjuster = new SphereRefractionAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageSwirlFilter) {
            this.adjuster = new SwirlAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageColorBalanceFilter) {
            this.adjuster = new ColorBalanceAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageLevelsFilter) {
            this.adjuster = new LevelsMinMidAdjuster().filter(gPUImageFilter);
        } else if (gPUImageFilter instanceof GPUImageBilateralFilter) {
            this.adjuster = new BilateralAdjuster().filter(gPUImageFilter);
        } else {
            this.adjuster = null;
        }
    }

    public boolean canAdjust() {
        return this.adjuster != null;
    }

    public void adjust(int i) {
        Adjuster<? extends GPUImageFilter> adjuster2 = this.adjuster;
        if (adjuster2 != null) {
            adjuster2.adjust(i);
        }
    }

    private abstract class Adjuster<T extends GPUImageFilter> {
        private T filter;

        private Adjuster() {
        }

        public abstract void adjust(int i);

        public float range(int i, float f, float f2) {
            return (((f2 - f) * ((float) i)) / 100.0f) + f;
        }

        public Adjuster<T> filter(GPUImageFilter gPUImageFilter) {
            this.filter = (T) gPUImageFilter;
            return this;
        }

        public T getFilter() {
            return this.filter;
        }


        public int range(int i, int i2, int i3) {
            return (((i3 - i2) * i) / 100) + i2;
        }
    }

    private class BilateralAdjuster extends Adjuster<GPUImageBilateralFilter> {
        private BilateralAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setDistanceNormalizationFactor(range(i, 0.0f, 15.0f));
        }
    }

    private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
        private BrightnessAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setBrightness(range(i, -1.0f, 1.0f));
        }
    }

    private class BulgeDistortionAdjuster extends Adjuster<GPUImageBulgeDistortionFilter> {
        private BulgeDistortionAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setRadius(range(i, 0.0f, 1.0f));
            (getFilter()).setScale(range(i, -1.0f, 1.0f));
        }
    }

    private class ColorBalanceAdjuster extends Adjuster<GPUImageColorBalanceFilter> {
        private ColorBalanceAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setMidtones(new float[]{range(i, 0.0f, 1.0f), range(i / 2, 0.0f, 1.0f), range(i / 3, 0.0f, 1.0f)});
        }
    }

    private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
        private ContrastAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setContrast(range(i, 0.0f, 2.0f));
        }
    }

    private class CrosshatchBlurAdjuster extends Adjuster<GPUImageCrosshatchFilter> {
        private CrosshatchBlurAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setCrossHatchSpacing(range(i, 0.0f, 0.06f));
            (getFilter()).setLineWidth(range(i, 0.0f, 0.006f));
        }
    }

    private class DissolveBlendAdjuster extends Adjuster<GPUImageDissolveBlendFilter> {
        private DissolveBlendAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setMix(range(i, 0.0f, 1.0f));
        }
    }

    private class EmbossAdjuster extends Adjuster<GPUImageEmbossFilter> {
        private EmbossAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setIntensity(range(i, 0.0f, 4.0f));
        }
    }

    private class ExposureAdjuster extends Adjuster<GPUImageExposureFilter> {
        private ExposureAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setExposure(range(i, -10.0f, 10.0f));
        }
    }

    private class GPU3x3TextureAdjuster extends Adjuster<GPUImage3x3TextureSamplingFilter> {
        private GPU3x3TextureAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setLineSize(range(i, 0.0f, 5.0f));
        }
    }

    private class GammaAdjuster extends Adjuster<GPUImageGammaFilter> {
        private GammaAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setGamma(range(i, 0.0f, 3.0f));
        }
    }

    private class GaussianBlurAdjuster extends Adjuster<GPUImageGaussianBlurFilter> {
        private GaussianBlurAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setBlurSize(range(i, 0.0f, 1.0f));
        }
    }

    private class GlassSphereAdjuster extends Adjuster<GPUImageGlassSphereFilter> {
        private GlassSphereAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setRadius(range(i, 0.0f, 1.0f));
        }
    }

    private class HazeAdjuster extends Adjuster<GPUImageHazeFilter> {
        private HazeAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setDistance(range(i, -0.3f, 0.3f));
            (getFilter()).setSlope(range(i, -0.3f, 0.3f));
        }
    }

    private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
        private HighlightShadowAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setShadows(range(i, 0.0f, 1.0f));
            (getFilter()).setHighlights(range(i, 0.0f, 1.0f));
        }
    }

    private class HueAdjuster extends Adjuster<GPUImageHueFilter> {
        private HueAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setHue(range(i, 0.0f, 360.0f));
        }
    }

    private class LevelsMinMidAdjuster extends Adjuster<GPUImageLevelsFilter> {
        private LevelsMinMidAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setMin(0.0f, range(i, 0.0f, 1.0f), 1.0f);
        }
    }

    private class MonochromeAdjuster extends Adjuster<GPUImageMonochromeFilter> {
        private MonochromeAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setIntensity(range(i, 0.0f, 1.0f));
        }
    }

    private class OpacityAdjuster extends Adjuster<GPUImageOpacityFilter> {
        private OpacityAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setOpacity(range(i, 0.0f, 1.0f));
        }
    }

    private class PixelationAdjuster extends Adjuster<GPUImagePixelationFilter> {
        private PixelationAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setPixel(range(i, 1.0f, 100.0f));
        }
    }

    private class PosterizeAdjuster extends Adjuster<GPUImagePosterizeFilter> {
        private PosterizeAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setColorLevels(range(i, 1, 50));
        }
    }

    private class RGBAdjuster extends Adjuster<GPUImageRGBFilter> {
        private RGBAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setRed(range(i, 0.0f, 1.0f));
            (getFilter()).setGreen(range(i, 0.0f, 1.0f));
            (getFilter()).setBlue(range(i, 0.0f, 1.0f));
        }
    }

    private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
        private SaturationAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setSaturation(range(i, 0.0f, 2.0f));
        }
    }

    private class SepiaAdjuster extends Adjuster<GPUImageSepiaFilter> {
        private SepiaAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setIntensity(range(i, 0.0f, 2.0f));
        }
    }

    private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
        private SharpnessAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setSharpness(range(i, -4.0f, 4.0f));
        }
    }

    private class SobelAdjuster extends Adjuster<GPUImageSobelEdgeDetection> {
        private SobelAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setLineSize(range(i, 0.0f, 5.0f));
        }
    }

    private class SphereRefractionAdjuster extends Adjuster<GPUImageSphereRefractionFilter> {
        private SphereRefractionAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setRadius(range(i, 0.0f, 1.0f));
        }
    }

    private class SwirlAdjuster extends Adjuster<GPUImageSwirlFilter> {
        private SwirlAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setAngle(range(i, 0.0f, 2.0f));
        }
    }

    private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
        private VignetteAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setVignetteStart(range(i, 0.0f, 1.0f));
        }
    }

    private class WhiteBalanceAdjuster extends Adjuster<GPUImageWhiteBalanceFilter> {
        private WhiteBalanceAdjuster() {
            super();
        }

        public void adjust(int i) {
            (getFilter()).setTemperature(range(i, 2000.0f, 8000.0f));
        }
    }
}
