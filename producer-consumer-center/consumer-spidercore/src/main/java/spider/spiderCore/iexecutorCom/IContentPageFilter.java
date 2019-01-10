/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package spider.spiderCore.iexecutorCom;

import commoncore.entity.httpEntity.ResponsePage;

/**
 * @author 一杯咖啡
 * @desc 筛选当前页面时候需要 传送到正文解析器 解析
 */
public interface IContentPageFilter {
    /**
     * desc:该接口用于将筛选当前页面 是否需要发送给后续解析器
     *
     * @param responsePage 数据
     **/
    void getContentPageData(ResponsePage responsePage);
}
