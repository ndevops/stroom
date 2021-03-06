/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.dashboard.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonPropertyOrder({"preferredSize", "tabs", "selected"})
@JsonInclude(Include.NON_EMPTY)
@XmlRootElement(name = "tabLayout")
@XmlType(name = "TabLayoutConfig", propOrder = {"preferredSize", "tabs", "selected"})
public class TabLayoutConfig extends LayoutConfig {
    private static final long serialVersionUID = -2105048053435792675L;

    /**
     * The preferred size of this layout in width, height.
     */
    @XmlElement(name = "preferredSize")
    @JsonProperty("preferredSize")
    private Size preferredSize = new Size();
    @XmlElementWrapper(name = "tabs")
    @XmlElements({@XmlElement(name = "tab", type = TabConfig.class)})
    @JsonProperty("tabs")
    private List<TabConfig> tabs;
    @XmlElement(name = "selected")
    @JsonProperty("selected")
    private Integer selected;

    public TabLayoutConfig() {
        // Default constructor necessary for GWT serialisation.
    }

    public TabLayoutConfig(final TabConfig... tabs) {
        if (tabs != null) {
            for (final TabConfig tab : tabs) {
                add(tab);
            }
        }
    }

    @Override
    public Size getPreferredSize() {
        return preferredSize;
    }

    @Override
    public void setPreferredSize(final Size preferredSize) {
        this.preferredSize = preferredSize;
    }

    public TabConfig get(final int index) {
        if (tabs != null) {
            final TabConfig tab = tabs.get(index);
            if (tab != null) {
                tab.setParent(this);
                return tab;
            }
        }
        return null;
    }

    public void add(final TabConfig tab) {
        if (tabs == null) {
            tabs = new ArrayList<>();
        }
        tabs.add(tab);
        tab.setParent(this);
    }

    public void add(final int index, final TabConfig tab) {
        if (tabs == null) {
            tabs = new ArrayList<>();
        }
        if (index <= tabs.size()) {
            tabs.add(index, tab);
        } else {
            tabs.add(tab);
        }
        tab.setParent(this);
    }

    public void remove(final TabConfig tab) {
        if (tabs != null) {
            tabs.remove(tab);
            tab.setParent(null);
        }
    }

    public int indexOf(final TabConfig tab) {
        if (tabs == null) {
            return -1;
        }
        return tabs.indexOf(tab);
    }

    public int count() {
        if (tabs == null) {
            return 0;
        }
        return tabs.size();
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(final Integer selected) {
        this.selected = selected;
    }
}
