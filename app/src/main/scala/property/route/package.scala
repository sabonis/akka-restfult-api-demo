package property

import property.message.response.Response

/**
  * Created by sabonis on 19/11/2016.
  */
package object route {

  val int2Response: Int => Response = a => Response(200, a.toString)

}
