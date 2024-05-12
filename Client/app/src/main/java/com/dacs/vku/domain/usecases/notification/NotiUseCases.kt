package com.dacs.vku.domain.usecases.notification

data class NotiUseCases(
    val getNotiDaoTao: GetNotiDaoTao,
    val getNotiCTSV: GetNotiCTSV,
    val getNotiKTDBCL: GetNotiKTDBCL,
    val getNotiKHTC: GetNotiKHTC
)
