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
package spider.spiderCore.fetcher.IFetcherTools;

import commoncore.entity.responseEntity.ResponsePage;

/**
 * 将数据交给解析器
 *
 * @author 一杯咖啡
 */
public interface DefaultContentPageFilter {
    /**
     * desc:该接口用于将请求返回的数据交给解析模块
     * @param  responsePage 数据
     **/
    void getContentPageData(ResponsePage responsePage);

}