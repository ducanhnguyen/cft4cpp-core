/*
 * Category.hh
 *
 * Copyright 2000, LifeLine Networks BV (www.lifeline.nl). All rights reserved.
 * Copyright 2000, Bastiaan Bakker. All rights reserved.
 *
 * See the COPYING file for the terms of usage and distribution.
 */

#ifndef _LOG4CPP_CATEGORY_HH
#define _LOG4CPP_CATEGORY_HH

#include <log4cpp/Portability.hh>
#include <log4cpp/Appender.hh>
#include <log4cpp/LoggingEvent.hh>
#include <log4cpp/Priority.hh>
#include <log4cpp/CategoryStream.hh>
#include <log4cpp/threading/Threading.hh>
#include <log4cpp/convenience.h>

#include <map>
#include <vector>
#include <cstdarg>
#include <stdexcept>

namespace log4cpp {
    class /*LOG4CPP_EXPORT*/ Category {
        typedef std::map<Appender *, bool> OwnsAppenderMap;
        public:       
}
#endif // _LOG4CPP_CATEGORY_HH
