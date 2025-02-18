document.getElementById("nav_bar").innerHTML = `
    <nav>
        <ul class="nav_bar_ul">
            <li class="li-horizontally"><a href="/Orders">Заявка</a></li>
            <li class="li-horizontally">
                <span class="dropdown">
                    <button class="dropbtn">Движения ТС
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <span class="dropdown-content">
                        <a href="/Acts/ActsDev">Акты приема-передачи ТС</a>
                        <a href="/anchDevice">Закрепление ТС</a>
                    </span>
                </span>
            </li>
            <li class="li-horizontally">
                <span class="dropdown">
                    <button class="dropbtn">Отчеты
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <span class="dropdown-content">
                        <a href="/Query/overdueOrders">Заявки более 5 дней</a>
                    </span>
                </span>
            </li>
            <li class="li-horizontally">
                <span class="dropdown">
                    <button class="dropbtn">Справочник
                        <i class="fa fa-caret-down"></i>
                    </button>
                    <span class="dropdown-content">
                        <a href="/device">Технические средства (ТС)</a>
                        <a href="/type">Тип ТС</a>
                        <a href="/model">Модель ТС</a>
                        <a href="/csa">КСА</a>
                    </span>
                </span>
            </li>
            <li class="li-horizontally">
                  <span class="dropdown">
                       <button class="dropbtn">Настройки
                            <i class="fa fa-caret-down"></i>
                       </button>
                       <span class="dropdown-content">
                            <a href="/regUser">Регистрация пользователя</a>
                       </span>
                  </span>
            </li>
        </ul>
    </nav>
    		<nav>
    				<span>
    					<h3 sec:authorize="isAuthenticated()" class="nav_bar_span_h" sec:authentication="name">Username</h3>
    				</span>
    		</nav>`;





