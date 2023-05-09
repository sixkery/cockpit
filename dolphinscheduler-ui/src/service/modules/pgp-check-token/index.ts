import {axios} from '@/service/service'
// import {pgpTokenReq} from "@/service/modules/pgp-check-token/types";
type pgpTokenReq={
    token?:string
}

export function pgpCheckToken(params: pgpTokenReq): any {
    return axios({
        url: '/pgp/check',
        method: 'get',
        params
    })
}
