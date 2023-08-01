import request from '@/utils/request'

export default {
    getHospSetList(current, limit, searchObj) {
        return request({
            url: `/admin/hosp/hospitalSet/findPageHospSet/${current}/${limit}`,
            method: 'post',
            data: searchObj
        })
    },

    deleteHospSetById(id) {
        return request({
            url: `/admin/hosp/hospitalSet/delete/${id}`,
            method: 'delete'
        })
    },

    batchRemoveHospSet(idList) {
        return request({
            url: `/admin/hosp/hospitalSet/batchRemove`,
            method: 'delete',
            data: idList
        })
    },

    lockHostSet(id, status) {
        return request({
            url: `/admin/hosp/hospitalSet/lockHospitalSet/${id}/${status}`,
            method: 'put',
        })
    },
    // 添加医院设置
    saveHospSet(hospitalSet) {
        return request ({
            url: `/admin/hosp/hospitalSet/saveHospitalSet`,
            method: 'post',
            data: hospitalSet
        })
    },
    // 院设置id查询
    getHospSet(id) {
        return request ({
            url: `/admin/hosp/hospitalSet/getHospitalSet/${id}`,
            method: 'get'
        })
    },
    // 修改医院设置
    updateHospSet(hospitalSet) {
        return request ({
            url: `/admin/hosp/hospitalSet/updateHospitalSet`,
            method: 'post',
            data: hospitalSet
        })
    }
}