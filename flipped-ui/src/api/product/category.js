import request from '@/utils/request'


export function listTree(query) {
  return request({
    url: 'product/flipped/category/list/tree',
    method: 'get',
    params: query
  })
}

export function deleteTreeNode(data) {
  return request({
    url: 'product/flipped/category/delete',
    method: 'post',
    data: data
  })
}
export function saveTreeNode(data) {
  return request({
    url: 'product/flipped/category/save',
    method: 'post',
    data: data
  })
}
