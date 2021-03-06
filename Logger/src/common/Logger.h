/*!

File: Logger.h
Author: Tan Zhe Xing (TrBBoI)
Version: 0.8.2015.02.05

The MIT License (MIT)

Copyright (c) 2015 Tan Zhe Xing

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

#ifndef TRB_COMMON_LOG_H_GUARD_
#define TRB_COMMON_LOG_H_GUARD_

#include <vector>
#include <string>
#include <cstdlib>
#include <cassert>
#include <iostream>
#include <type_traits>
#include "Singleton.h"

namespace trb {

class Log;

class LogListener {
public:
  virtual void info( const char *str ) = 0;
  virtual void debug( const char *str ) = 0;
  virtual void error( const char *str ) = 0;
  virtual void fatal( const char *str ) = 0;
};

class Log : protected Singleton<Log> {
public:
  struct Entry {
    std::string name;
    LogListener *listener;
  };
  typedef std::vector<Entry> ContainerType;

  Log() {
    Entry entry;
    entry.name = "Default";
    entry.listener = new StdLogListener;

    listeners.push_back( entry );
  }

private:
  ContainerType listeners;

  template <typename ...Args>
  static void _strcat( std::string &str, const char *s, Args... );
  template <typename U, typename T>
  static bool _is_same( const T &listener );

public:
  template <typename Listener>
  static void Add( const char *name );
  template <typename Listener>
  static void Remove( const char *name );

  template <typename ...Args>
  static void Info( const char *s, Args... );
  template <typename ...Args>
  static void Debug( const char *s, Args... );
  template <typename ...Args>
  static void Error( const char *s, Args... );
  template <typename ...Args>
  static void Fatal( const char *s, Args... );

};

class StdLogListener : public LogListener {
public:
  virtual void info( const char *str ) {
    std::cout << str << std::endl; 
  }

  virtual void debug( const char *str ) {
    std::cout << str << std::endl;
  }

  virtual void error( const char *str ) {
    std::cout << str << std::endl;
  }

  virtual void fatal( const char *str ) {
    std::cout << str << std::endl;
  }
};

}

template <typename ...Args>
void trb::Log::_strcat( std::string &str, const char *s, Args... args ) {
  const int MAX_BUFF = 256;
  char buffer[MAX_BUFF];
  int len = 0;

  do {
    len = snprintf( buffer, MAX_BUFF, s, args... );
    str += buffer;
  } while ( len >= MAX_BUFF - 1 );
}
template <typename U, typename T>
bool trb::Log::_is_same( const T &t ) {
  return std::is_same<T, U>::value;
}

template <typename Listener>
void trb::Log::Add( const char *name ) {

  static_assert( std::is_base_of<LogListener, Listener>::value, "Listener is not base of LogListener.");

  Entry entry;
  entry.name = name;
  entry.listener = new Listener;

  Log::instance().listeners.push_back( entry );
}

template <typename Listener>
void trb::Log::Remove( const char *name ) {
  Log &log = Log::instance();
  ContainerType::iterator iter = log.listeners.begin();
  for ( ; iter != log.listeners.end() ; ++iter ) {
    if ( _is_same<Listener>( iter->listener ) && iter->name == name ) {
      log.listeners.erase( iter );
      break;
    }
  }
}

template <typename ...Args>
void trb::Log::Info( const char *s, Args ...args ) {
  std::string str;
  _strcat( str, s, args... );

  Log &log = Log::instance();
  for ( Entry &item : log.listeners ) {
    item.listener->info( str.c_str() );
  }
}

template <typename ...Args>
void trb::Log::Debug( const char *s, Args ...args ) {
  std::string str;
  _strcat( str, s, args... );

  Log &log = Log::instance();
  for ( Entry &item : log.listeners ) {
    item.listener->debug( str.c_str() );
  }
}

template <typename ...Args>
void trb::Log::Error( const char *s, Args ...args ) {
  std::string str;
  _strcat( str, s, args... );

  Log &log = Log::instance();
  for ( Entry &item : log.listeners ) {
    item.listener->error( str.c_str() );
  }
}

template <typename ...Args>
void trb::Log::Fatal( const char *s, Args ...args ) {
  std::string str;
  _strcat( str, s, args... );

  Log &log = Log::instance();
  for ( ContainerType::iterator iter : log.listeners ) {
    iter->listener->fatal( str.c_str() );
  }
}

#endif // #ifndef TRB_COMMON_LOG_H_GUARD_
