package com.xue.liang.app.v3.bean.ring;

import comtom.com.realtimestream.bean.Term;

/**
 * Created by jikun on 17/4/26.
 */

public class RingBean {
    private boolean isChecked = false;
    private Term term;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
