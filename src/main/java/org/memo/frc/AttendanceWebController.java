package org.memo.frc;

import java.sql.Date;
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

	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST},value="report")
	public String attendanceTimeFramePOST(@ModelAttribute ReportRequest request, Model model) {
		if(request.isEmpty()){
			model.addAttribute("appName", appName);
			model.addAttribute("reportRequest", new ReportRequest(new Date(new java.util.Date().getTime()), new Date(new java.util.Date().getTime())));
		}else{
			model.addAttribute("appName", appName);
			model.addAttribute("dateHolder", request);
			List<Attendance> reportData = attendanceService.attendanceTimeFrame(request.getStart(), request.getEnd());
			model.addAttribute("reportData", reportData);
		}
		return "attendanceReport";
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

	@GetMapping("/dwnld-weeklyhours")
	ResponseEntity<String> downloadWeeklyHourse() {
		List<Attendance> weeklyHours = attendanceService.weeklyHours();
		String dlm = ",";
		StringBuffer buf = new StringBuffer("Name,Hours\n");
		double total = 0;
		for (Attendance pojo : weeklyHours) {
			buf.append(pojo.getName() + dlm + pojo.getTimeSpent() + "\n");
			total += pojo.getTimeSpent();
		}
		buf.append("============,============\n");
		buf.append("Total" + dlm + total + "\n");

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
