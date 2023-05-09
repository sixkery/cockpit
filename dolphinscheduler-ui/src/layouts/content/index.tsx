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

import {defineComponent, onMounted, watch, toRefs, ref} from 'vue'
import {NLayout, NLayoutContent, NLayoutHeader, useMessage} from 'naive-ui'
import NavBar from './components/navbar'
import SideBar from './components/sidebar'
import {useDataList} from './use-dataList'
import {useLocalesStore} from '@/store/locales/locales'
import {useRouteStore} from '@/store/route/route'
import {useI18n} from 'vue-i18n'
import {Router, useRoute, useRouter} from 'vue-router'
import {pgpCheckToken} from "@/service/modules/pgp-check-token";
import {SessionIdRes} from "@/service/modules/login/types";
import {useUserStore} from "@/store/user/user";
import {UserInfoRes} from "@/service/modules/users/types";
import {getUserInfo} from "@/service/modules/users";
import {useTimezoneStore} from "@/store/timezone/timezone";

const Content = defineComponent({
  name: 'DSContent',
  setup() {
    window.$message = useMessage()

    const route = useRoute()
    const {locale} = useI18n()
    const localesStore = useLocalesStore()
    const routeStore = useRouteStore()
    const userStore = useUserStore()
    const timezoneStore = useTimezoneStore()
    const router: Router = useRouter()
    const {
      state,
      changeMenuOption,
      changeHeaderMenuOptions,
      changeUserDropdown
    } = useDataList()
    const sideKeyRef = ref()

    onMounted(() => {
      locale.value = localesStore.getLocales
      changeMenuOption(state)
      changeHeaderMenuOptions(state)
      getSideMenu(state)
      changeUserDropdown(state)
    })

    const getSideMenu = (state: any) => {
      const key = route.meta.activeMenu
      state.sideMenuOptions =
        state.menuOptions.filter((menu: { key: string }) => menu.key === key)[0]
          ?.children || state.menuOptions
      state.isShowSide = route.meta.showSide
    }

    watch(useI18n().locale, () => {
      changeMenuOption(state)
      changeHeaderMenuOptions(state)
      getSideMenu(state)
      changeUserDropdown(state)
    })

    watch(
      () => route.path,
      async () => {
        // pgp token check
        if (route.fullPath.includes('token')) {
          const token = route.query.token + '';
          const loginRes: SessionIdRes = await pgpCheckToken({token});
          await userStore.setSessionId(loginRes.sessionId)
          const userInfoRes: UserInfoRes = await getUserInfo()
          await userStore.setUserInfo(userInfoRes)
          const timezone = userInfoRes.timeZone ? userInfoRes.timeZone : 'UTC'
          await timezoneStore.setTimezone(timezone)
          const path = route.path
          router.push({path: path || 'home'})
        }
        if (route.path !== '/login') {
          routeStore.setLastRoute(route.path)

          state.isShowSide = route.meta.showSide as boolean
          if (route.matched[1].path === '/projects/:projectCode') {
            changeMenuOption(state)
          }

          getSideMenu(state)

          const currentSide = (
            route.meta.activeSide
              ? route.meta.activeSide
              : route.matched[1].path
          ) as string
          sideKeyRef.value = currentSide.includes(':projectCode')
            ? currentSide.replace(
              ':projectCode',
              route.params.projectCode as string
            )
            : currentSide
        }
      },
      {immediate: true}
    )

    return {
      ...toRefs(state),
      changeMenuOption,
      sideKeyRef
    }
  },
  render() {
    return (
      <NLayout style='height: 100%'>
        <NLayoutHeader style='height: 65px'>
          <NavBar
            class='tab-horizontal'
            headerMenuOptions={this.headerMenuOptions}
            localesOptions={this.localesOptions}
            timezoneOptions={this.timezoneOptions}
            userDropdownOptions={this.userDropdownOptions}
          />
        </NLayoutHeader>
        <NLayout has-sider position='absolute' style='top: 65px'>
          {this.isShowSide && (
            <SideBar
              sideMenuOptions={this.sideMenuOptions}
              sideKey={this.sideKeyRef}
            />
          )}
          <NLayoutContent
            native-scrollbar={false}
            style='padding: 16px 22px'
            contentStyle={'height: 100%'}
          >
            <router-view key={this.$route.fullPath}/>
          </NLayoutContent>
        </NLayout>
      </NLayout>
    )
  }
})

export default Content
