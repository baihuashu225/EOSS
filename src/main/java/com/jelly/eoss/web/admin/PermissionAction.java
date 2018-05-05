package com.jelly.eoss.web.admin;

import com.jelly.eoss.dao.BaseDao;
import com.jelly.eoss.model.AdminPermission;
import com.jelly.eoss.util.ComUtil;
import com.jelly.eoss.util.Const;
import com.jelly.eoss.util.IdGenerator;
import com.jelly.eoss.util.Pager;
import com.jelly.eoss.web.BaseAction;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/system/permission")
public class PermissionAction extends BaseAction {
	@Autowired
	private BaseDao baseDao;

	@RequestMapping(value = "/toList")
	public ModelAndView toList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Integer page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		Map<String, String> param = this.getRequestMap(request);
		RowBounds rb = new RowBounds((page -1) * Const.PAGE_SIZE, Const.PAGE_SIZE);
		
		Integer totalRow = this.baseDao.mySelectOne("_EXT.Permission_QueryPermissionPage_Count", param);
		List<Map<String, Object>> dataList = this.baseDao.getSqlSessionTemplate().selectList("_EXT.Permission_QueryPermissionPage", param, rb);
		
		Pager pager = new Pager(page.intValue(), Const.PAGE_SIZE, totalRow.intValue());
		pager.setData(dataList);
		
		request.setAttribute("pager", pager);
		this.resetAllRequestParams(request);
		return new ModelAndView("/system/permissionList.jsp");
	}

	@RequestMapping(value = "/toAdd")
	public ModelAndView toAdd(HttpServletRequest request, HttpServletResponse response, AdminPermission permission) throws Exception{
		return new ModelAndView("/system/permissionAdd.jsp");
	}
	
	@RequestMapping(value = "/add")
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response, AdminPermission permission) throws Exception{
		String id = IdGenerator.id();
		permission.setId(id);
		this.baseDao.myInsert(AdminPermission.Insert, permission);
		request.getRequestDispatcher("/system/permission/toList").forward(request, response);
		return null;
	}
	
	@RequestMapping(value = "/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
//		Log.Debug("id:" + id);
		this.baseDao.myDelete(AdminPermission.DeleteByPk, id);
		response.getWriter().write("y");
	}
	
	@RequestMapping(value = "/toUpdate")
	public ModelAndView toUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");

		AdminPermission permission = this.baseDao.mySelectOne(AdminPermission.SelectByPk, id);

		request.setAttribute("permission", permission);
		return new ModelAndView("/system/permissionUpdate.jsp");
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response, AdminPermission permission) throws ServletException, IOException{
		this.baseDao.myUpdate(AdminPermission.Update, permission);

		request.setAttribute("permission", permission);
        request.getRequestDispatcher("/system/permission/toList").forward(request, response);
		return null;
	}
	
	//getter and setter
	public BaseDao getBaseDao() {
		return baseDao;
	}
}