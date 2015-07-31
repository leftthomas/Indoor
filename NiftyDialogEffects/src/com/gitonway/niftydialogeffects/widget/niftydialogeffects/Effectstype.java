package com.gitonway.niftydialogeffects.widget.niftydialogeffects;

import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.BaseEffects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FadeIn;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Fall;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FlipH;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.FlipV;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.NewsPaper;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.RotateBottom;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.RotateLeft;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Shake;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SideFall;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideBottom;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideLeft;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideRight;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.SlideTop;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.effects.Slit;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
