package com.cyrof.form;

import com.cyrof.validators.DatetimeInput;
import com.cyrof.validators.DatetimeStartEndValueCompare;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;


@DatetimeStartEndValueCompare.List({
        @DatetimeStartEndValueCompare(start = "start", end = "end", message = "{error.startDateBeforeEndDate}"),
})

public class Trackform {

    @Email
    @NotEmpty
    private String email;

    @DatetimeInput(message = "{error.datepattern.validator}")
    private String start;

    @DatetimeInput(message = "{error.datepattern.validator}")
    private String end;

    private String offset;

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
