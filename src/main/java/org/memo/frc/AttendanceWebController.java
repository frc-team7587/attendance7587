package org.memo.frc;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST}, value="details")
	public String attendanceTimeFramePOST(@ModelAttribute ReportRequest request, Model model) {
		if(request.isEmpty()){
			model.addAttribute("appName", appName);
			model.addAttribute("reportRequest", new ReportRequest(Date.valueOf(LocalDate.now().minusDays(7)), Date.valueOf(LocalDate.now())));
		}else{
			model.addAttribute("appName", appName);
			List<Attendance> records = attendanceService.attendanceTimeFrame(request.getStart(), request.getEnd());
			model.addAttribute("attendanceRecords", records);
		}
		return "attendanceDetails";
	}

	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST}, value="report")
	public String weeklyHours(@ModelAttribute ReportRequest request, Model model) {
		model.addAttribute("appName", appName);
		if(request.isEmpty()){
			model.addAttribute("reportRequest", new ReportRequest(Date.valueOf(LocalDate.now().minusDays(7)), Date.valueOf(LocalDate.now())));
		}else{
			List<Attendance> records = attendanceService.getSummary(request.getStart(), request.getEnd());
			double total = 0;
			for (Attendance pojo : records) {
				total += pojo.getTimeSpent();
			}
			model.addAttribute("reportData", records);
			model.addAttribute("total", String.format("%.2f", total));
		}

		return "attendanceReport";
	}

	@GetMapping("/downloadsummary/{start}/{end}")
	ResponseEntity<String> downloadSummary(@PathVariable("start") String start, @PathVariable("end") String end) {
		ReportRequest request = null;
		try{
			request = new ReportRequest(start ,end);
		}catch(ParseException e){
			throw new RuntimeException("Invalid date format");
		}
		
		List<Attendance> records = attendanceService.getSummary(request.getStart(), request.getEnd());
		String dlm = ",";
		StringBuffer buf = new StringBuffer("Team 7587 Attendance Summary\n")
			.append("Start Date: " + dlm + request.getStart() + "\n")
			.append("End Date: " + dlm + request.getEnd() + "\n\n");
		double total = 0;
		for (Attendance rec : records) {
			buf.append(rec.getName() + dlm + rec.getTimeSpent() + "\n");
			total += rec.getTimeSpent();
		}
		buf.append("\nTotal:" + dlm + String.format("%.2f", total) + "\n");

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Attendance_team7587.csv\"")
				.body(buf.toString());
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
}
