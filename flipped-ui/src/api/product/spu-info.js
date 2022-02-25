import request from '@/utils/request'

// 查询spu信息列表
export function listInfo(query) {
  return request({
    url: '/product/flipped/spu/info/list',
    method: 'get',
    params: query
  })
}

// 查询spu信息详细
export function getInfo(id) {
  return request({
    url: '/product/flipped/spu/info/' + id,
    method: 'get'
  })
}

// 新增spu信息
export function addInfo(data) {
  return request({
    url: '/product/flipped/spu/info',
    method: 'post',
    data: data
  })
}

// 修改spu信息
export function updateInfo(data) {
  return request({
    url: '/product/flipped/spu/info',
    method: 'put',
    data: data
  })
}

// 删除spu信息
export function delInfo(id) {
  return request({
    url: '/product/flipped/spu/info/' + id,
    method: 'delete'
  })
}

export function up(id) {
  return request({
    url: '/product/flipped/spu/info/'+id+'/up',
    method: 'post'
  })
}
