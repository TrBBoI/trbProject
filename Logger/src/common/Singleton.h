// version 1.0
#ifndef TRB_COMMON_SINGLETON_H_GUARD_
#define TRB_COMMON_SINGLETON_H_GUARD_

namespace trb {

template <typename T>
class Singleton {
public:
  static T &instance() {
    static T singleton;
    return singleton;
  }
protected:
  Singleton() {}
  virtual ~Singleton() {}

private:
  Singleton( const Singleton & ) = delete;
  Singleton( Singleton && ) = delete;
  Singleton &operator=( const Singleton & ) = delete;
  Singleton &&operator=( Singleton && ) = delete;
};

} // namespace trb
#endif // #ifndef TRB_COMMON_SINGLETON_H_GUARD_
