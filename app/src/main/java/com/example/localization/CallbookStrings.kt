package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    NEPALI("ne", "Nepali", "नेपाली")
}

object CallbookStrings {
    fun appName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "CALLBOOK"
        AppLanguage.NEPALI -> "कल बुक"
    }

    fun appTagline(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Connect, Share & Discover"
        AppLanguage.NEPALI -> "जोडिनुहोस्, साझा गर्नुहोस् र रमाउनुहोस्"
    }

    fun signInTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Welcome to CALLBOOK"
        AppLanguage.NEPALI -> "कल बुकमा स्वागत छ"
    }

    fun signInSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sign in with your Google account to connect with real friends and discover authentic posts."
        AppLanguage.NEPALI -> "वास्तविक साथीहरूसँग जोडिन र पोस्टहरू हेर्न आफ्नो गुगल खाताबाट साइन इन गर्नुहोस्।"
    }

    fun signInWithGoogle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sign in with Google"
        AppLanguage.NEPALI -> "गुगल मार्फत साइन इन गर्नुहोस्"
    }

    fun registerTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Complete Your Profile"
        AppLanguage.NEPALI -> "आफ्नो प्रोफाइल पूरा गर्नुहोस्"
    }

    fun registerSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Set up your verified profile with your name and mobile number to start posting on CALLBOOK."
        AppLanguage.NEPALI -> "कल बुकमा पोस्ट गर्न आफ्नो नाम र मोबाइल नम्बर प्रविष्ट गर्नुहोस्।"
    }

    fun fullName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Full Name"
        AppLanguage.NEPALI -> "पूरा नाम"
    }

    fun emailAddress(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Email Address"
        AppLanguage.NEPALI -> "इमेल ठेगाना"
    }

    fun mobileNumber(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Mobile Number"
        AppLanguage.NEPALI -> "मोबाइल नम्बर"
    }

    fun mobileHint(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "e.g. 9812345678"
        AppLanguage.NEPALI -> "उदा: ९८१२३४५६७८"
    }

    fun bio(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Bio / Status (Optional)"
        AppLanguage.NEPALI -> "परिचय / स्थिति (ऐच्छिक)"
    }

    fun saveProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Save Profile"
        AppLanguage.NEPALI -> "प्रोफाइल सुरक्षित गर्नुहोस्"
    }

    fun saving(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Saving..."
        AppLanguage.NEPALI -> "सुरक्षित गर्दै..."
    }

    fun feed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Feed"
        AppLanguage.NEPALI -> "फिड"
    }

    fun createPost(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Create Post"
        AppLanguage.NEPALI -> "पोस्ट सिर्जना गर्नुहोस्"
    }

    fun whatsOnYourMind(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "What's on your mind?"
        AppLanguage.NEPALI -> "तपाईंको मनमा के छ?"
    }

    fun addPhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Add Photo"
        AppLanguage.NEPALI -> "तस्वीर थप्नुहोस्"
    }

    fun removePhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Remove Photo"
        AppLanguage.NEPALI -> "तस्वीर हटाउनुहोस्"
    }

    fun publish(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Publish Post"
        AppLanguage.NEPALI -> "प्रकाशित गर्नुहोस्"
    }

    fun publishing(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Publishing..."
        AppLanguage.NEPALI -> "प्रकाशित हुँदैछ..."
    }

    fun like(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Like"
        AppLanguage.NEPALI -> "लाइक"
    }

    fun comments(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Comments"
        AppLanguage.NEPALI -> "टिप्पणीहरू"
    }

    fun writeComment(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Write a comment..."
        AppLanguage.NEPALI -> "टिप्पणी लेख्नुहोस्..."
    }

    fun postComment(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Send"
        AppLanguage.NEPALI -> "पठाउनुहोस्"
    }

    fun noCommentsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No comments yet. Start the conversation!"
        AppLanguage.NEPALI -> "कुनै टिप्पणी छैन। पहिलो टिप्पणी गर्नुहोस्!"
    }

    fun noPostsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No posts yet. Be the first to publish a post on CALLBOOK!"
        AppLanguage.NEPALI -> "अहिलेसम्म कुनै पोस्ट छैन। कल बुकमा पहिलो पोस्ट गर्नुहोस्!"
    }

    fun editProfile(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Edit Profile"
        AppLanguage.NEPALI -> "प्रोफाइल सम्पादन"
    }

    fun signOut(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Sign Out"
        AppLanguage.NEPALI -> "साइन आउट"
    }

    fun changeLanguage(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Switch Language"
        AppLanguage.NEPALI -> "भाषा बदल्नुहोस्"
    }

    fun choosePhoto(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Choose Photo"
        AppLanguage.NEPALI -> "तस्वीर छान्नुहोस्"
    }

    fun profileUpdated(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Profile updated successfully!"
        AppLanguage.NEPALI -> "प्रोफाइल सफलतापूर्वक अद्यावधिक भयो!"
    }

    fun postCreated(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Post published successfully!"
        AppLanguage.NEPALI -> "पोस्ट सफलतापूर्वक प्रकाशित भयो!"
    }

    fun delete(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Delete"
        AppLanguage.NEPALI -> "मेटाउनुहोस्"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.NEPALI -> "रद्द गर्नुहोस्"
    }

    fun close(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Close"
        AppLanguage.NEPALI -> "बन्द गर्नुहोस्"
    }

    fun justNow(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Just now"
        AppLanguage.NEPALI -> "भर्खरै"
    }

    fun minutesAgo(minutes: Long, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "${minutes}m ago"
        AppLanguage.NEPALI -> "${minutes} मिनेट अघि"
    }

    fun hoursAgo(hours: Long, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "${hours}h ago"
        AppLanguage.NEPALI -> "${hours} घण्टा अघि"
    }

    fun daysAgo(days: Long, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "${days}d ago"
        AppLanguage.NEPALI -> "${days} दिन अघि"
    }

    fun adminBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Admin"
        AppLanguage.NEPALI -> "प्रशासक"
    }

    fun verifiedBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Verified"
        AppLanguage.NEPALI -> "प्रमाणित"
    }

    fun developerCredit(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Developer: Bir Pujara"
        AppLanguage.NEPALI -> "विकासकर्ता: वीर पुजारा"
    }

    fun adminPanel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Admin Verification Panel"
        AppLanguage.NEPALI -> "प्रशासक प्रमाणीकरण प्यानल"
    }

    fun requestVerification(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Request Blue Verification Badge"
        AppLanguage.NEPALI -> "निलो प्रमाणीकरण ब्याजको लागि अनुरोध गर्नुहोस्"
    }

    fun verificationRequested(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Verification Request Pending"
        AppLanguage.NEPALI -> "प्रमाणीकरण अनुरोध विचाराधीन छ"
    }

    fun approve(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Verify User"
        AppLanguage.NEPALI -> "प्रमाणित गर्नुहोस्"
    }

    fun revoke(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Revoke Badge"
        AppLanguage.NEPALI -> "ब्याज हटाउनुहोस्"
    }

    fun noUsersFound(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No other users registered yet."
        AppLanguage.NEPALI -> "अहिलेसम्म अन्य कुनै प्रयोगकर्ता दर्ता भएका छैनन्।"
    }

    fun installApp(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Install App"
        AppLanguage.NEPALI -> "एप इन्स्टल गर्नुहोस्"
    }

    fun installAppTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Install CALLBOOK on Android"
        AppLanguage.NEPALI -> "एन्ड्रोइडमा कल बुक इन्स्टल गर्नुहोस्"
    }

    fun installAppSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Install on your home screen or download the APK directly."
        AppLanguage.NEPALI -> "आफ्नो होम स्क्रिनमा इन्स्टल गर्नुहोस् वा सिधै APK डाउनलोड गर्नुहोस्।"
    }

    fun chromePwaGuideTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Install via Google Chrome (Instant PWA)"
        AppLanguage.NEPALI -> "गुगल क्रोम मार्फत इन्स्टल (तत्काल PWA)"
    }

    fun chromePwaStep1(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "1. Open the public link in Google Chrome on your phone."
        AppLanguage.NEPALI -> "१. आफ्नो फोनमा गुगल क्रोम खोलेर सार्वजनिक लिङ्क खोल्नुहोस्।"
    }

    fun chromePwaStep2(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "2. Tap the Chrome menu icon (⋮) in the top-right corner."
        AppLanguage.NEPALI -> "२. माथिल्लो दायाँ कुनामा रहेको क्रोम मेनु (⋮) मा थिच्नुहोस्।"
    }

    fun chromePwaStep3(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "3. Tap 'Install app' or 'Add to Home screen' to add CALLBOOK."
        AppLanguage.NEPALI -> "३. 'एप इन्स्टल गर्नुहोस्' वा 'होम स्क्रिनमा थप्नुहोस्' मा थिच्नुहोस्।"
    }

    fun publicWebLink(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Public Sharable Link"
        AppLanguage.NEPALI -> "सार्वजनिक साझा गर्न मिल्ने लिङ्क"
    }

    fun copyLink(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Copy Public Link"
        AppLanguage.NEPALI -> "सार्वजनिक लिङ्क प्रतिलिपि गर्नुहोस्"
    }

    fun linkCopied(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Public link copied to clipboard!"
        AppLanguage.NEPALI -> "सार्वजनिक लिङ्क क्लिपबोर्डमा प्रतिलिपि गरियो!"
    }

    fun offlineSupportBadge(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Offline Support Enabled: Cached feed is always accessible."
        AppLanguage.NEPALI -> "अफलाइन समर्थन सक्षम: क्यास गरिएको फिड सधैं उपलब्ध हुन्छ।"
    }

    fun openInBrowser(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Open Public Web Link"
        AppLanguage.NEPALI -> "सार्वजनिक वेब लिङ्क खोल्नुहोस्"
    }

    fun developerBirPujara(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Developer: Bir Pujara"
        AppLanguage.NEPALI -> "विकासकर्ता: वीर पुजारा"
    }
}

