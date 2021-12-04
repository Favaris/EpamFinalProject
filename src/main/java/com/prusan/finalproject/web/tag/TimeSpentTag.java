package com.prusan.finalproject.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Tag for converting time retrieved as an attribute 'minutes' to two input tags for hours and minutes.<br>
 * <pre>
 *     Attributes:
 *     'minutes' - minutes to be converted in hours and minutes
 *     'minutesLabel' - a label that will go after the minutes count was printed
 *     'hoursLabel' - a label that will go after the hours count was printed
 * </pre>
 */
public class TimeSpentTag extends SimpleTagSupport {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    private int minutes;
    private String minutesLabel;
    private String hoursLabel;

    public void setMinutesLabel(String minutesLabel) {
        this.minutesLabel = minutesLabel;
    }

    public void setHoursLabel(String hoursLabel) {
        this.hoursLabel = hoursLabel;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
        log.debug("got set minutes field as '{}'", minutes);
    }

    @Override
    public void doTag() throws JspException, IOException {
        int hours = minutes / 60;
        log.debug("retrieved an hours part of amount of minutes, hours={}", hours);
        minutes = minutes % 60;
        log.debug("retrieved a minutes remainder, minutes={}", minutes);

        getJspContext().getOut().print(String.format("%d %s %d %s", hours, hoursLabel, minutes, minutesLabel));

    }
}
