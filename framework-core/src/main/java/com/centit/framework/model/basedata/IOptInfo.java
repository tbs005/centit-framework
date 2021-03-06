package com.centit.framework.model.basedata;

/**
 *  业务模块说明
 * @author codefan@hotmail.com
 */
public interface IOptInfo {
	/**
	 * 业务ID 主键
	 * @return optId
	 */
     String getOptId();
    /**
     * 父业务ID
     * @return 父业务ID
     */
     String getPreOptId();
    /**
     * 业务名称
     * @return 业务名称
     */
     String getOptName();
    
    /**
     * S:实施业务, O:普通业务, W:流程业务, I:项目业务
     * @return OptType
     */
     String getOptType();
    /**
     * 业务菜单其实地址 url
     * @return 业务菜单其实地址 url
     */
     String getOptRoute();
    /**
     * 后台权限控制业务url前缀（不是必须的）
     * @return 后台权限控制业务url前缀
     */
     String getOptUrl();
    
    /**
     * 是否是菜单项，Y:是 N:否
     * @return 是否是菜单项，Y:是 N:否
     */    
     String getIsInToolbar();
    /**
     * 图标编号
     * @return 图标编号
     */
     Long getImgIndex();
    
    /**
     * 页面打开方式 D: DIV I： iFrame
     * @return 页面打开方式 D: DIV I： iFrame
     */
     String getPageType();
    /**
     * 业务排序号
     * @return 业务排序号
     */
     Long getOrderInd();
}
