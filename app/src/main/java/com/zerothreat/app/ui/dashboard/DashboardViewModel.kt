package com.zerothreat.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zerothreat.app.data.UrlRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: UrlRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    init {
        if (repository != null) {
            loadRealData()
        } else {
            _uiState.value = createDummyState()
        }
    }

    private fun loadRealData() {
        viewModelScope.launch {
            combine(
                repository!!.allUrls,
                repository.threats,
                repository.urlCount,
                repository.threatsCount
            ) { allUrls, threats, urlCount, threatsCount ->
                val safeCount = urlCount - threatsCount
                val threatsBySource = threats.groupBy { it.source }
                
                val recentAlerts = allUrls.take(5).map { checkedUrl ->
                    ThreatAlert(
                        id = checkedUrl.id.toString(),
                        url = checkedUrl.url,
                        severity = when (checkedUrl.threatLevel) {
                            "PHISHING" -> ThreatSeverity.PHISHING
                            "SUSPICIOUS" -> ThreatSeverity.SUSPICIOUS
                            else -> ThreatSeverity.SAFE
                        },
                        source = checkedUrl.source,
                        timeAgo = formatTimestamp(checkedUrl.timestamp)
                    )
                }

                DashboardState(
                    isProtected = true,
                    totalLinksAnalyzed = urlCount,
                    threatsDetected = threatsCount,
                    threatsBlocked = threatsCount,
                    safeLinks = safeCount,
                    threatSources = ThreatSources(
                        sms = threatsBySource["SMS"]?.size ?: 0,
                        browser = threatsBySource["Browser"]?.size ?: 0,
                        notifications = threatsBySource["Notification"]?.size ?: 0
                    ),
                    recentAlerts = recentAlerts,
                    trendData = listOf(2, 5, 3, 8, 6, 4, 7), // Placeholder for now
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60_000 -> "Just now"
            diff < 3600_000 -> "${diff / 60_000}m ago"
            diff < 86400_000 -> "${diff / 3600_000}h ago"
            else -> "${diff / 86400_000}d ago"
        }
    }

    private fun createDummyState(): DashboardState {
        return DashboardState(
            isProtected = true,
            totalLinksAnalyzed = 1247,
            threatsDetected = 38,
            threatsBlocked = 35,
            safeLinks = 1209,
            threatSources = ThreatSources(
                sms = 15,
                browser = 18,
                notifications = 5
            ),
            recentAlerts = listOf(
                ThreatAlert(
                    id = "1",
                    url = "https://fake-bank-login.phish.com",
                    severity = ThreatSeverity.PHISHING,
                    source = "SMS",
                    timeAgo = "2h ago"
                ),
                ThreatAlert(
                    id = "2",
                    url = "https://suspicious-link.xyz/offer",
                    severity = ThreatSeverity.SUSPICIOUS,
                    source = "Browser",
                    timeAgo = "5h ago"
                ),
                ThreatAlert(
                    id = "3",
                    url = "https://malware-download.net",
                    severity = ThreatSeverity.PHISHING,
                    source = "Notification",
                    timeAgo = "1d ago"
                ),
                ThreatAlert(
                    id = "4",
                    url = "https://google.com",
                    severity = ThreatSeverity.SAFE,
                    source = "Browser",
                    timeAgo = "2d ago"
                ),
                ThreatAlert(
                    id = "5",
                    url = "https://scam-prize.win/claim",
                    severity = ThreatSeverity.PHISHING,
                    source = "SMS",
                    timeAgo = "3d ago"
                )
            ),
            trendData = listOf(2, 5, 3, 8, 6, 4, 7), // Last 7 days
            isLoading = false
        )
    }
}