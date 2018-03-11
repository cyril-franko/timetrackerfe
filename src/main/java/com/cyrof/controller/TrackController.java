package com.cyrof.controller;

import com.cyrof.form.Trackform;
import com.cyrof.configuration.MainProperties;
import com.cyrof.timetracker.integration.api.RecordsApi;
import com.cyrof.timetracker.integration.model.Track;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
public class TrackController {

    private static final Logger logger = LogManager.getLogger(TrackController.class);

    private DateTimeFormatter formatter;
    private DateTimeFormatter formatterToShow;
    private DateTimeFormatter formatterInput;

    String inputPattern;

    private final RecordsApi api;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MainProperties mainProperties;

    public TrackController(RecordsApi api, @Value("${datepatern.fromTimetracker}") String timetrackerpattern,
                           @Value("${datepatern.toShow}") String toShow,
                           @Value("${datepatern.input}") String inputPattern
                             ) {
        this.api = api;
        this.formatter = DateTimeFormatter.ofPattern(timetrackerpattern);
        this.formatterToShow = DateTimeFormatter.ofPattern(toShow);
        this.formatterInput = DateTimeFormatter.ofPattern(inputPattern);
        this.inputPattern = inputPattern;
    }


    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        return "welcome";
    }

    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute Trackform record, Model model) {

        List<Track> trackList = null;

        int offset = record !=null && record.getOffset() !=null ? Integer.parseInt(record.getOffset()) : 0;

        boolean finish = false;

        while (!finish) {
            try {
                trackList = api.getRecords(offset, mainProperties.getTrackPerList(), record.getEmail());
                model.addAttribute("next", offset + mainProperties.getTrackPerList());

                List<Track> records = trackList != null ? trackList.stream().filter(r -> r != null)
                        .map(tractToRecord)
                        .collect(Collectors.toList()) : null;
                model.addAttribute("records", records);

                if (offset > 0 && records.size() == 0) {
                    offset -= mainProperties.getTrackPerList();
                } else {
                    finish = true;
                }

            } catch (ResourceAccessException raex) {
                logger.error(raex);
                model.addAttribute("message", messageSource.getMessage("error.connect_to_timetracker", null, Locale.getDefault()));
                finish = true;
            } catch (Exception ex) {
                model.addAttribute("next", offset);
                model.addAttribute("message", messageSource.getMessage("info.max_records", null, Locale.getDefault()));
                offset -= mainProperties.getTrackPerList();
            }

        }

        model.addAttribute("email", record.getEmail());

        if (offset>0) {
            model.addAttribute("prev", offset-mainProperties.getTrackPerList());
        }
        model.addAttribute("offset", offset);
        return new ModelAndView("welcome");
    }

    @RequestMapping(value="/newtrack", method= RequestMethod.GET)
    public String newTrack(Trackform post, Model model) {
        model.addAttribute("inputPh", this.inputPattern);
        return "addrecord";
    }

    @RequestMapping(value = "/newtrack", method = RequestMethod.POST)
    public String addNewTrack(@Valid Trackform post, BindingResult bindingResult, Model model) {
        model.addAttribute("inputPh", this.inputPattern);
        if (bindingResult.hasErrors()) {
            return "addrecord";
        }
        model.addAttribute("email", post.getEmail());
        model.addAttribute("start", post.getStart());
        model.addAttribute("end", post.getEnd());
        try {
            api.recordsPost(post.getEmail(), post.getStart(), post.getEnd());
            model.addAttribute("message", messageSource.getMessage("save.success", null, Locale.getDefault()));
        } catch (ResourceAccessException raex) {
            logger.error(raex);
            model.addAttribute("message", messageSource.getMessage("error.connect_to_timetracker", null, Locale.getDefault()));
            return "addrecord";
        }
        return "welcome";
    }

    Function<Track, Track> tractToRecord
            = new Function<Track, Track>() {

        public Track apply(Track t) {
            Track record = new Track();
            record.setEmail(t.getEmail());
            record.setStart(LocalDateTime.parse(t.getStart(), formatter).format(formatterToShow));
            record.setEnd(LocalDateTime.parse(t.getEnd(), formatter).format(formatterToShow));
            return record;
        }
    };
}
