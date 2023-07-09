import http from "@/utils/http";

/**
* (${entityName})表API接口
*
* @author pikaGenerator
* @since ${generateTime}
*/

/**
* 分页查询所有数据
*
* @param page 当前页
* @param size 页大小
* @param ${name} 查询条件
* @return 所有数据
*/
export const selectAll = (page, size, ${name}) => {
  return http({
    url: `/web/${name}/${page}/${size}`,
    method: `get`,
    params: ${name},
  });
};
/**
* 通过主键查询单条数据
*
* @param id 主键
* @return 单条数据
*/
export const selectOne = (id) => {
  return http({
    url: `/web/${name}/${id}`,
    method: `get`,
  });
};
/**
* 新增数据
*
* @param ${name} 实体对象
* @return 新增结果
*/
export const insert = (${name}) => {
  return http({
    url: `/web/${name}`,
    method: `post`,
    data: ${name},
  });
};
/**
* 修改数据
*
* @param ${name} 实体对象
* @return 修改结果
*/
export const update = (${name}) => {
  return http({
    url: `/web/${name}`,
    method: `put`,
    data: ${name},
  });
};
/**
* 删除数据
*
* @param idList 主键集合
* @return 删除结果
*/
export const deleteByIds = (ids) => {
  return http({
    url: `/web/${name}`,
    method: `delete`,
    params: {
      idList: ids,
    },
  });
};
export default {
  selectAll,
  selectOne,
  insert,
  update,
  deleteByIds,
};
