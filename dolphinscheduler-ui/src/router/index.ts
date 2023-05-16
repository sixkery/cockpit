/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {
  createRouter,
  createWebHistory,
  NavigationGuardNext,
  RouteLocationNormalized
} from 'vue-router'
import routes from './routes'
import { useUserStore } from '@/store/user/user'
import type { UserInfoRes } from '@/service/modules/users/types'

// NProgress
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {SessionIdRes} from "@/service/modules/login/types";
import {pgpCheckToken} from "@/service/modules/pgp-check-token";
import {getUserInfo} from "@/service/modules/users";
import {useTimezoneStore} from "@/store/timezone/timezone";

const router = createRouter({
  history: createWebHistory(
    import.meta.env.MODE === 'production' ? '/dolphinscheduler/ui/' : '/'
  ),
  routes
})

interface metaData {
  title?: string
  activeMenu?: string
  showSide?: boolean
  auth?: Array<string>
}

/**
 * Routing to intercept
 */
router.beforeEach(
  async (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    NProgress.start()
    const userStore = useUserStore()
    const metaData: metaData = to.meta
    const timezoneStore = useTimezoneStore()

    // pgp check token
    if (to.fullPath.includes('token')) {
      const token = to.query.token + '';
      const loginRes: SessionIdRes = await pgpCheckToken({token});
      await userStore.setSessionId(loginRes.sessionId)
      const userInfoRes: UserInfoRes = await getUserInfo()
      await userStore.setUserInfo(userInfoRes)
      const timezone = userInfoRes.timeZone ? userInfoRes.timeZone : 'UTC'
      await timezoneStore.setTimezone(timezone)
      const path = to.path
      router.push({path: path || 'home'})
    }
    if (
      metaData.auth?.includes('ADMIN_USER') &&
      (userStore.getUserInfo as UserInfoRes).userType !== 'ADMIN_USER' &&
      metaData.activeMenu === 'security'
    ) {
      to.fullPath = '/security/token-manage'
      next({ name: 'token-manage' })
    } else {
      next()
    }

    NProgress.done()
  }
)

router.afterEach(() => {
  NProgress.done()
})

export default router
