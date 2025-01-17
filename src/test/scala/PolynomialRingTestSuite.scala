// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge,
// publish, distribute, sublicense, and/or sell copies of the Software,
// and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import org.scalatest.funsuite.AnyFunSuite

class PolynomialRingTestSuite extends AnyFunSuite {

  import algebra.PolynomialRing._

  val max_num_tests = 1000

  type POLY = Map[Int,Double]

  test("one"){
    for{n <- 0 to 5
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        } {
      assert(polyAlmostEqual(polyMultiply(polyOne,p),p))
      assert(polyAlmostEqual(polyMultiply(p,polyOne),p))
    }
  }

  test("zero"){
    for{n <- 0 to 5
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        } {
      assert(polyAlmostEqual(polyAdd(polyZero,p),p))
      assert(polyAlmostEqual(polyAdd(p,polyZero),p))
    }
  }

  test("polyAlmostEqual"){
    assert(polyAlmostEqual(Map(),Map(),0.0))
    assert(polyAlmostEqual(Map(1 -> 10.001),
                           Map(1 -> 10.002),
                           0.1))
    assert(polyAlmostEqual(Map(1 -> 0.0,
                               2 -> 10.0),
                           Map(2 -> 10.0,
                               3 -> 0.0),
                           0.0))

    for{n <- 0 to 5
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        } {
      assert(polyAlmostEqual(p,p,0.0))
    }

  }

  test("polyAdd"){
    for{n <- 0 to 5
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        q = randomPoly(n)
        } {
      assert(polyAlmostEqual(polyZero,polyAdd(p,polyScale(-1.0,p))))
      assert(polyAlmostEqual(polyAdd(p,q),
                             polyAdd(q,p)))
    }
  }

  test("polyScale") {
    for {n <- 0 to 5
         _ <- 0 to max_num_tests
         p = randomPoly(n)
         } {
      assert(polyAlmostEqual(polyScale(1.0,p),
                             p))
      assert(polyAlmostEqual(polyAdd(polyScale(2.0,p),
                                     polyScale(-3.0,p)),
                             polyScale(-1.0,p)))
    }
  }

  test("polySubtract") {
    for {n <- 0 to 5
         _ <- 0 to max_num_tests
         p = randomPoly(n)
         q = randomPoly(n)
         } {
      assert(polyAlmostEqual(polySubtract(polyScale(3.0, p),
                                          polyScale(2.0, p)),
                             p))
      assert(polyAlmostEqual(polySubtract(p, q),
                             polyScale(-1.0, polySubtract(q, p))))
    }
  }


  test("polyMultiply"){
    for{n <- 0 to 5
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        q = randomPoly(n)
        r = randomPoly(n)
        } {
      assert(polyAlmostEqual(p,polyMultiply(p,polyOne)))
      assert(polyAlmostEqual(polyMultiply(p,q),
                             polyMultiply(q,p)))
      assert(polyAlmostEqual(polyMultiply(p,polyMultiply(q,r)),
                             polyMultiply(polyMultiply(p,q),r)))
    }
  }

  test("polyFastPower"){
    for{n <- 0 to 5
        m <- 0 to 7
        _ <- 0 to max_num_tests
        p = randomPoly(n)
        fast = polyFastPower(p,m)
        slow = polySlowPower(p,m)
        } {
      assert(polyAlmostEqual(fast,
                             slow,
                             0.01),
             s"m=$m expecting small difference: ${polySubtract(fast,slow)}")
    }
  }
}
