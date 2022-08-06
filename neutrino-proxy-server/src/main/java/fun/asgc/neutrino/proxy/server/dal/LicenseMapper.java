/**
 * Copyright (c) 2022 aoshiguchen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fun.asgc.neutrino.proxy.server.dal;

import fun.asgc.neutrino.core.annotation.Component;
import fun.asgc.neutrino.core.annotation.Param;
import fun.asgc.neutrino.core.db.annotation.Delete;
import fun.asgc.neutrino.core.db.annotation.Update;
import fun.asgc.neutrino.core.db.mapper.SqlMapper;
import fun.asgc.neutrino.proxy.server.dal.entity.LicenseDO;

import java.util.Date;

/**
 *
 * @author: aoshiguchen
 * @date: 2022/8/6
 */
@Component
public interface LicenseMapper extends SqlMapper {

	/**
	 * 新增license
	 * @param license
	 */
	int add(LicenseDO license);

	@Update("update `license` set enable = :enable where id = :id")
	void updateEnableStatus(@Param("id") Integer id, @Param("enable") Integer enable);

	@Update("update `license` set key = :key,update_time = :updateTime where id = :id")
	void reset(@Param("id") Integer id, @Param("key") String key, @Param("updateTime") Date updateTime);

	@Delete("delete from `license` where id = ?")
	void delete(Integer id);
}
