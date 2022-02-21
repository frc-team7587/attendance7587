package org.memo.frc;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AttendanceWebController {

	private Logger logger = LoggerFactory.getLogger(AttendanceWebController.class);

	@Value("${spring.application.name}")
	private String appName;

	@Autowired
	private AttendanceService attendanceService;

	@GetMapping({ "/" })
	public String currentlyCheckedIn(Model model) {
		List<Attendance> currentlyCheckedIn = attendanceService.currentlyCheckedIn();
		model.addAttribute("appName", appName);
		model.addAttribute("currentlyCheckedIn", currentlyCheckedIn);
		return "currentlyCheckedIn";
	}

	@GetMapping({ "attendanceTimeFrame" })
	public String attendanceTimeFrame(Model model) {
		model.addAttribute("appName", appName);
		model.addAttribute("dateHolder", new AttendanceTimeFrame());
		return "attendanceTimeFrame";
	}

	@PostMapping({ "attendanceTimeFramePost" })
	public String attendanceTimeFramePOST(@ModelAttribute AttendanceTimeFrame frame, Model model) {
		model.addAttribute("appName", appName);
		System.out.println("start: " + frame.getStart().toString() + "\nend: " + frame.getEnd().toString());
		List<Attendance> attendanceTimeFrame = attendanceService.attendanceTimeFrame(frame.getStart(), frame.getEnd());
		model.addAttribute("attendanceTimeFrame", attendanceTimeFrame);
		return "attendanceTimeFramePost";
	}

	@GetMapping({ "/allAttendance" })
	public String allAttendancePage(Model model) {

		model.addAttribute("appName", appName);

		List<Attendance> allAttendance = attendanceService.getAllAttendance();
		model.addAttribute("allAttendance", allAttendance);

		return "allAttendance";
	}

	@GetMapping({ "weeklyHours" })
	public String weeklyHours(Model model) {
		List<Attendance> weeklyHours = attendanceService.weeklyHours();
		double total = 0;
		for (Attendance pojo : weeklyHours) {
			total += pojo.getTimeSpent();
		}
		model.addAttribute("appName", appName);
		model.addAttribute("weeklyHours", weeklyHours);
		model.addAttribute("total", total);

		return "weeklyHours";
	}

	@PostMapping({ "weeklyHours" })
	public String weeklyHours(@ModelAttribute Date date, Model model) {
		List<Attendance> weeklyHours = date == null ? attendanceService.weeklyHours()
				: attendanceService.weeklyHours(date);
		double total = 0;
		for (Attendance pojo : weeklyHours) {
			total += pojo.getTimeSpent();
		}
		model.addAttribute("appName", appName);
		model.addAttribute("weeklyHours", weeklyHours);
		model.addAttribute("total", total);

		return "weeklyHours";
	}

	@GetMapping({ "attendanceByName/{name}" })
	public String attendanceByName(@PathVariable("name") String name, Model model) {
		List<Attendance> attendanceByName = attendanceService.getAttendanceByName(name);
		model.addAttribute("appName", appName);
		model.addAttribute("attendanceByName", attendanceByName);
		return "attendanceByName";
	}

	@GetMapping({ "scanCheck/{name}" })
	public String insertPage(@PathVariable("name") String name, Model model) {
		Attendance result = attendanceService.scanCheck(name);
		model.addAttribute("appName", appName);
		model.addAttribute("scanResult", result);
		model.addAttribute("isCheckIn", attendanceService.getIsCheckIn());
		return "scanCheck";
	}

	@PostMapping({ "/confirmCheck" })
	public String confirmCheck(@ModelAttribute Attendance att, Model model) {
		model.addAttribute("appName", appName);
		model.addAttribute("scanResult", attendanceService.confirmCheck(att));
		return "confirmCheck";
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
					setValue(new Timestamp(parsedDate.getTime()));
				} catch (ParseException e) {
					setValue(null);
				}
			}
		});
	}
}
