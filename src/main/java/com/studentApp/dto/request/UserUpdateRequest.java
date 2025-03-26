package com.studentApp.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserUpdateRequest {
	private String tenDangNhap;
	private String matKhau;
	private String email;
	private Integer idPhanQuyen;
	private Date ngayCapNhat;
}
