package com.useai.core.ui

import com.useai.core.model.report.ExperienceReportType

val ExperienceReportType.displayName: String
    get() = when (this) {
        ExperienceReportType.PART_TIME -> "아르바이트"
        ExperienceReportType.INTERN -> "인턴"
        ExperienceReportType.FULL_TIME -> "정규직"
        ExperienceReportType.CONTRACT -> "계약직"
        ExperienceReportType.VOLUNTEER -> "봉사 활동"
        ExperienceReportType.AWARD -> "수상경력"
        ExperienceReportType.CLUB -> "동아리 활동"
        ExperienceReportType.RESEARCH -> "연구 활동"
        ExperienceReportType.MILITARY -> "군복무"
        ExperienceReportType.PERSONAL -> "개인 활동"
        ExperienceReportType.UNKNOWN -> "기타"
    }

val ExperienceReportType.description: String
    get() = when(this) {
        ExperienceReportType.PART_TIME -> "서비스 현장에서 고객과 직접 소통하며 쌓은 실전 감각이 돋보입니다."
        ExperienceReportType.INTERN -> "실무 현장을 직접 경험하며 조직의 시스템과 업무 흐름을 빠르게 익혔습니다."
        ExperienceReportType.FULL_TIME -> "풍부한 실무 경험을 바탕으로 조직의 성과를 이끌어온 준비된 전문가입니다."
        ExperienceReportType.CONTRACT -> "주어진 기간 내에 목표를 완수하며 실무 역량과 책임감을 입증해왔습니다."
        ExperienceReportType.VOLUNTEER -> "사회적 가치를 실현하고 타인을 배려하며 쌓은 선한 영향력이 느껴집니다."
        ExperienceReportType.AWARD -> "치열한 경쟁 속에서 남다른 성과를 내며 객관적인 역량의 우수성을 증명했습니다."
        ExperienceReportType.CLUB -> "동료들과 공동의 목표를 향해 협력하며 팀워크의 가치를 경험했습니다."
        ExperienceReportType.RESEARCH -> "특정 분야를 깊이 있게 탐구하고 분석하여 학술적 전문성을 쌓아왔습니다."
        ExperienceReportType.MILITARY -> "엄격한 환경 속에서도 맡은 임무를 성실히 수행하며 강한 책임감을 길렀습니다."
        ExperienceReportType.PERSONAL -> "스스로 목표를 설정하고 끝까지 완수해낸 자기주도적 실행력이 훌륭합니다."
        ExperienceReportType.UNKNOWN -> ""
    }
