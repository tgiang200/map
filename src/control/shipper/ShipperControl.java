package control.shipper;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import control.order.OrderModel;


@Controller
@RequestMapping(value = "/shipper")
public class ShipperControl {

	@RequestMapping(value = "/formRegister")
	public String formRegister() {
		return "shipper/registerShipper";
	}

	@RequestMapping(value = "/register")
	public String registerShipper(Model model, HttpServletRequest request, HttpServletResponse respone) 
										throws UnsupportedEncodingException {
		
		request.setCharacterEncoding("UTF-8");
		
		String email = request.getParameter("email");
		String fullName = request.getParameter("fullname"); 
		String address = request.getParameter("address");
		String idCard = request.getParameter("idCard");
		String phone = request.getParameter("phone");
		String dateOfBirth = request.getParameter("dateOfBirth");
		String facebook = request.getParameter("facebook");
		String vehicle = request.getParameter("vehicle");
		String vehicleNumber = request.getParameter("vehicleNumber");
		//String convey = request.getParameter("convey")+"";
		String password = request.getParameter("password");
		String status = "waiting";
		String funds = "20000";

		BasicDBObject shipper = new BasicDBObject();

		shipper.put("_id", phone);
		shipper.put("fullname", fullName);
		shipper.put("address", address);
		shipper.put("idCard", idCard);
		shipper.put("phone", phone);
		shipper.put("email", email);
		shipper.put("dateOfBirth", dateOfBirth);
		shipper.put("facebook", facebook);
		shipper.put("vehicle", vehicle);
		shipper.put("vehicleNumber", vehicleNumber);
		//shipper.put("convey", convey);
		shipper.put("password", password);
		shipper.put("statusConfirm", status);
		shipper.put("funds", funds);

		boolean resultInsert = new ShipperModel().insertShipper(shipper);
		if (resultInsert) {
			model.addAttribute("result", "Đăng kí thành công, đang chờ duyệt");
		} else {
			model.addAttribute("result", "Số điện thoại này đã được sử dụng để đăng ký");
			return "shipper/resultRegister";
		}

		return "shipper/resultRegister";
	}

	// API đăng kí shipper
	@RequestMapping(value = "/api/registerShipper/phone={phone}&fullname={fullname}&address={address}&idCard={idCard}&vehicle={vehicle}&"
			+ "email={email}&dateOfBirth={dateOfBirth}&facebook={facebook}&vehicleNumber={vehicleNumber}&password={password}", 
			method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> apiRegister(Model model, HttpServletRequest request, @PathVariable String phone,
			@PathVariable String fullname, @PathVariable String address, @PathVariable String idCard, @PathVariable String dateOfBirth, 
			@PathVariable String vehicle, @PathVariable String email, @PathVariable String facebook,
			@PathVariable String vehicleNumber, @PathVariable String password)
			throws JSONException {

		String status = "waiting";

		BasicDBObject shipper = new BasicDBObject();

		shipper.put("_id", phone);
		shipper.put("fullname", fullname);
		shipper.put("address", address.replace("..", "/"));
		shipper.put("idCard", idCard);
		shipper.put("phone", phone);
		shipper.put("email", email);
		shipper.put("dateOfBirth", dateOfBirth);
		shipper.put("facebook", facebook);
		shipper.put("vehicle", vehicle);
		shipper.put("vehicleNumber", vehicleNumber);
		shipper.put("password", password);
		//shipper.put("convey", convey);
		shipper.put("statusConfirm", status);
		shipper.put("funds", "0");

		JSONObject respone = new JSONObject();
		boolean result = new ShipperModel().insertShipper(shipper);
		if (result) {
			respone.put("result", "success");
			respone.put("message", "success");
		} else {
			respone.put("result", "failed");
			respone.put("message", "Phone is existing");
		}
		return new ResponseEntity<String>(respone.toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/shipperID={shipperID}")
	public String infoUser(Model model, @PathVariable String shipperID) {
		String phone = shipperID;
		// Lay thong tin producer duoc xac nhan
		ShipperModel shipperModel = new ShipperModel();
		String status = shipperModel.getStatus(shipperID);
		if (status.equals("waiting")||status.equals("confirming")){
			DBCursor cursor = shipperModel.queryShipper(phone);
			List<DBObject> list = cursor.toArray();
			String str = list.toString();
			model.addAttribute("shipper", str);
			// Chuyen trang thai xac nhan cua producer sang "confirming"
			shipperModel.updateStatusConfirm(phone, "confirming");
			// tra ve trang thong tin chi tiet de tien hanh xac nhan
			return "shipper/infoShipper";
		} else {
			DBCursor cursor = shipperModel.queryShipper(phone);
			List<DBObject> list = cursor.toArray();
			String str = list.toString();
			model.addAttribute("shipper", str);
			// Chuyen trang thai xac nhan cua producer sang "confirming"
			//shipperModel.updateStatusConfirm(phone, "confirming");
			// tra ve trang thong tin chi tiet de tien hanh xac nhan
			return "shipper/infoShipper";
		}
	}

	@RequestMapping(value = "/listConfirm")
	public String listConfirm(Model model, HttpServletRequest request, HttpServletResponse respone) {
		// Lay thong tin producer duoc xac nhan
		DBCursor cursor = new ShipperModel().listConfirm();
		List<DBObject> list = cursor.toArray();
		String str = list.toString();
		model.addAttribute("listShipper", str);
		return "shipper/listConfirmShipper";
	}
	
	@RequestMapping(value = "/listShipperConfirmed")
	public String listShipperConfirmed(Model model, HttpServletRequest request, HttpServletResponse respone) {
		// Lay thong tin producer duoc xac nhan
		JSONArray list = new ShipperModel().queryShipperStatus("confirmed");
		String str = list.toString();
		model.addAttribute("listShipper", str);
		return "shipper/listShipperConfirmed";
	}


	@RequestMapping(value = "/resultConfirm/action={action}&shipperID={shipperID}")
	public String resultConfirm(Model model, @PathVariable String action, @PathVariable String shipperID) {
		String shipperPhone = shipperID;
		ShipperModel shipperModel = new ShipperModel();
		if (action.equals("confirmed")) {
			// doi trang thai thanh da xac nhan
			shipperModel.updateStatusConfirm(shipperPhone, "confirmed");
		} else if (action.equals("notConfirm")) {
			// doi trang thai thanh tu choi
			//shipperModel.updateStatusConfirm(shipperPhone, "notConfirm");
			shipperModel.deleteShipper(shipperID);
		} else {
			// khi action la cancel thi tra ve trang thai dang cho xac nhan
			shipperModel.updateStatusConfirm(shipperPhone, "waiting");
		}

		DBCursor cursor = shipperModel.listConfirm();
		List<DBObject> list = cursor.toArray();
		String str = list.toString();
		model.addAttribute("listShipper", str);

		return "shipper/redirectListConfirm";
	}

	// API xac nhan da chuyen hang
	@RequestMapping(value = "/transported/shipperID={shipperID}&orderID={orderID}")
	public ResponseEntity<String> changePassword(@PathVariable String shipperID, @PathVariable String orderID)
			throws JSONException {
		boolean resultUpdate = new OrderModel().updateStatus(orderID, "transported");
		JSONObject respone = new JSONObject();
		if (resultUpdate) {
			respone.put("result", "success");
		} else {
			respone.put("result", "failed");
		}
		return new ResponseEntity<String>(respone.toString(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update")
	public String updateShipper(Model model, HttpServletRequest request, HttpServletResponse respone) 
										throws UnsupportedEncodingException {
		
		request.setCharacterEncoding("UTF-8");
		
		String email = request.getParameter("email");
		String fullName = request.getParameter("fullname"); 
		String address = request.getParameter("address");
		String idCard = request.getParameter("idCard");
		String phone = request.getParameter("phone");
		String dateOfBirth = request.getParameter("dateOfBirth");
		String facebook = request.getParameter("facebook");
		String vehicle = request.getParameter("vehicle");
		String vehicleNumber = request.getParameter("vehicleNumber");
		//String convey = request.getParameter("convey")+"";
		String password = request.getParameter("password");
		String funds = request.getParameter("funds");
		String status = "waiting";

		BasicDBObject shipper = new BasicDBObject();

		//shipper.put("_id", phone);
		shipper.put("fullname", fullName);
		shipper.put("address", address);
		shipper.put("idCard", idCard);
		shipper.put("phone", phone);
		shipper.put("email", email);
		shipper.put("dateOfBirth", dateOfBirth);
		shipper.put("facebook", facebook);
		shipper.put("vehicle", vehicle);
		shipper.put("vehicleNumber", vehicleNumber);
		//shipper.put("convey", convey);
		shipper.put("password", password);
		shipper.put("statusConfirm", status);
		shipper.put("funds", funds);
		
		boolean resultInsert = new ShipperModel().updateShipper(shipper, phone);
		if (resultInsert) {
			model.addAttribute("result", "Cập nhật thành công, đăng nhập lại để tiếp tục");
		} else {
			model.addAttribute("result", "Không thể cập nhật, vui lòng thử lại sau");
			return "shipper/resultRegister";
		}

		return "shipper/resultRegister";
	}
}