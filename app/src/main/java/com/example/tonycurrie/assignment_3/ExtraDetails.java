package com.example.tonycurrie.assignment_3;


public class ExtraDetails {

    public String heading;
    public String overView;
    ExtraDetails(String heading, String overView )
    {
        this.heading = heading;
        this.overView = overView;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getHeading() {
        return heading;
    }

    public String getOverView() {
        return overView;
    }

}


